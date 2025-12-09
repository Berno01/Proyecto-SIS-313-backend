# Sistema de Auditoría - Módulo de Ventas

## Descripción General

Se ha implementado un sistema de auditoría reutilizable para rastrear la creación y modificación de registros en la base de datos. Este sistema permite saber quién y cuándo se creó o modificó una venta.

## Estructura Implementada

### 1. Clase Base de Auditoría (AuditEntity)

**Ubicación:** `common/audit/AuditEntity.java`

```java
@MappedSuperclass
public abstract class AuditEntity {
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", updatable = false)
    private Integer createdBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private Integer updatedBy;
}
```

**Características:**

- `@MappedSuperclass`: Permite que las entidades hijas hereden estos campos
- `updatable = false` en created\_\* : Protege estos campos contra modificaciones accidentales
- Campos mapeados a snake_case en la base de datos (created_at, created_by, etc.)

### 2. Actualización de VentaEntity

**Cambio:** Ahora extiende `AuditEntity`

```java
public class VentaEntity extends AuditEntity {
    // ... campos existentes ...
}
```

Los campos de auditoría se heredan automáticamente, no es necesario declararlos explícitamente.

### 3. Actualización de VentaDTO

**Nuevo campo agregado:**

```java
@JsonProperty("id_usuario")
private Integer idUsuario;
```

Este campo se envía desde el frontend con el ID del usuario que está realizando la operación.

### 4. Actualización del Modelo de Dominio (Venta)

**Nuevo campo:**

```java
private Integer idUsuario; // ID del usuario para auditoría
```

**Constructor actualizado:**

```java
public Venta(Integer idVenta, String nombreCliente, LocalDateTime fechaVenta,
             List<DetalleVenta> detalleVenta, Integer idUsuario) {
    // ... inicialización ...
}
```

### 5. Lógica de Auditoría en VentaRepositoryAdapterMySql

#### Al CREAR una venta (id_venta == null):

```java
if (ventaEntity.getId_venta() == null) {
    ventaEntity.setCreatedAt(LocalDateTime.now());
    ventaEntity.setCreatedBy(ventaDominio.getIdUsuario());
    // updatedAt y updatedBy quedan en null
}
```

#### Al ACTUALIZAR una venta (id_venta != null):

```java
else {
    VentaEntity ventaExistente = repository.findById(...).orElse(null);
    if (ventaExistente != null) {
        // CRÍTICO: Preservar campos originales de creación
        ventaEntity.setCreatedAt(ventaExistente.getCreatedAt());
        ventaEntity.setCreatedBy(ventaExistente.getCreatedBy());
    }
    // Actualizar campos de modificación
    ventaEntity.setUpdatedAt(LocalDateTime.now());
    ventaEntity.setUpdatedBy(ventaDominio.getIdUsuario());
}
```

## Base de Datos

### Estructura de la tabla venta actualizada:

```sql
CREATE TABLE venta (
    id_venta INT AUTO_INCREMENT PRIMARY KEY,
    nombre_cliente VARCHAR(100),
    fecha_venta DATETIME,
    total DOUBLE,
    descuento_total DOUBLE,
    estadoVenta BOOLEAN,

    -- Campos de auditoría
    created_at DATETIME,
    created_by INT,
    updated_at DATETIME,
    updated_by INT
);
```

### Script de Migración:

Ubicado en: `src/main/resources/migrationsdb/venta_audit_migration.sql`

```sql
ALTER TABLE venta
ADD COLUMN created_at DATETIME NULL,
ADD COLUMN created_by INT NULL,
ADD COLUMN updated_at DATETIME NULL,
ADD COLUMN updated_by INT NULL;
```

## Flujo de Operaciones

### Crear una Venta:

1. Frontend envía `VentaDTO` incluyendo `id_usuario`
2. Controller → Service → Adapter
3. Adapter detecta que `id_venta == null` (es nueva)
4. Se setean:
   - `created_at` = now()
   - `created_by` = id_usuario del DTO
   - `updated_at` = null
   - `updated_by` = null
5. Se guarda en BD

**Request ejemplo:**

```json
{
  "nombre_cliente": "Juan Pérez",
  "total_venta": 150.00,
  "descuento_total": 10.00,
  "id_usuario": 1,
  "detalle_venta": [...]
}
```

### Actualizar una Venta:

1. Frontend envía `VentaDTO` incluyendo `id_venta` y `id_usuario`
2. Controller → Service → Adapter
3. Adapter detecta que `id_venta != null` (ya existe)
4. Se recupera la venta existente de BD
5. Se preservan:
   - `created_at` (valor original)
   - `created_by` (valor original)
6. Se actualizan:
   - `updated_at` = now()
   - `updated_by` = id_usuario del DTO
7. Se guarda en BD

**Request ejemplo:**

```json
{
  "id_venta": 5,
  "nombre_cliente": "Juan Pérez Actualizado",
  "total_venta": 180.00,
  "descuento_total": 15.00,
  "id_usuario": 2,
  "detalle_venta": [...]
}
```

## Protecciones Implementadas

### 1. Protección a nivel JPA:

```java
@Column(name = "created_at", updatable = false)
@Column(name = "created_by", updatable = false)
```

Estos campos NO pueden ser actualizados por JPA después de la creación.

### 2. Protección a nivel Lógica:

Al actualizar, se recuperan los valores originales de la BD y se reasignan explícitamente:

```java
ventaEntity.setCreatedAt(ventaExistente.getCreatedAt());
ventaEntity.setCreatedBy(ventaExistente.getCreatedBy());
```

## Reutilización Futura

Para agregar auditoría a otras entidades:

### 1. Extender AuditEntity:

```java
public class CompraEntity extends AuditEntity {
    // campos específicos de compra
}
```

### 2. Agregar id_usuario al DTO:

```json
{
  "id_usuario": 1
  // otros campos...
}
```

### 3. Implementar lógica en el Adapter:

```java
if (entity.getId() == null) {
    // Lógica de creación
    entity.setCreatedAt(LocalDateTime.now());
    entity.setCreatedBy(dominio.getIdUsuario());
} else {
    // Lógica de actualización
    Entity existente = repository.findById(...);
    entity.setCreatedAt(existente.getCreatedAt());
    entity.setCreatedBy(existente.getCreatedBy());
    entity.setUpdatedAt(LocalDateTime.now());
    entity.setUpdatedBy(dominio.getIdUsuario());
}
```

## Consultas de Auditoría

### Listar ventas con información de auditoría:

```sql
SELECT
    v.id_venta,
    v.nombre_cliente,
    v.total,
    v.created_at,
    u1.username as creado_por,
    v.updated_at,
    u2.username as actualizado_por
FROM venta v
LEFT JOIN usuario u1 ON v.created_by = u1.id_usuario
LEFT JOIN usuario u2 ON v.updated_by = u2.id_usuario;
```

### Ventas modificadas recientemente:

```sql
SELECT * FROM venta
WHERE updated_at IS NOT NULL
ORDER BY updated_at DESC
LIMIT 10;
```

### Ventas creadas por un usuario específico:

```sql
SELECT * FROM venta
WHERE created_by = 1;
```

## Consideraciones Importantes

1. **Siempre enviar id_usuario**: El frontend debe incluir el id_usuario del usuario autenticado en cada request.

2. **Validación**: Considerar agregar validación para asegurar que id_usuario no sea null.

3. **Zona horaria**: Los timestamps se guardan en la zona horaria del servidor.

4. **Performance**: Los índices en created_at y updated_at mejoran las consultas de auditoría.

5. **Integridad referencial**: Considerar agregar foreign keys de created_by/updated_by hacia la tabla usuario.

## Próximos Pasos

- [ ] Ejecutar el script de migración SQL
- [ ] Actualizar el frontend para enviar id_usuario en cada request
- [ ] Aplicar el mismo patrón a otras entidades (Compra, Repuesto, etc.)
- [ ] Crear reportes de auditoría
- [ ] Implementar validaciones de seguridad (verificar que el id_usuario sea válido)
