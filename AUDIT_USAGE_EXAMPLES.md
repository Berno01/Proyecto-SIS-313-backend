# Ejemplos de Uso - Sistema de Auditoría

## 1. Crear una Nueva Venta

### Request (POST /api/ventas)

```json
{
  "nombre_cliente": "María González",
  "total_venta": 250.5,
  "descuento_total": 25.0,
  "id_usuario": 1,
  "detalle_venta": [
    {
      "cantidad": 2,
      "precio_unitario_repuesto": 100.0,
      "precio_sugerido_repuesto": 125.0,
      "id_repuesto": 5,
      "costo_repuesto": 80.0
    },
    {
      "cantidad": 1,
      "precio_unitario_repuesto": 50.5,
      "precio_sugerido_repuesto": 60.0,
      "id_repuesto": 8,
      "costo_repuesto": 40.0
    }
  ]
}
```

### Lo que sucede internamente:

1. **VentaController** recibe el DTO
2. **VentaMapper** convierte DTO → Domain (pasando id_usuario)
3. **VentaService** ejecuta lógica de negocio
4. **VentaRepositoryAdapter** detecta que es nueva (id_venta == null)
5. Se setean campos de auditoría:
   ```java
   created_at = "2025-12-09 14:30:15"
   created_by = 1
   updated_at = null
   updated_by = null
   ```

### Registro en Base de Datos:

```sql
INSERT INTO venta (
  nombre_cliente, fecha_venta, total, descuento_total, estadoVenta,
  created_at, created_by, updated_at, updated_by
) VALUES (
  'María González', '2025-12-09 14:30:15', 250.50, 25.00, 1,
  '2025-12-09 14:30:15', 1, NULL, NULL
);
```

---

## 2. Actualizar una Venta Existente

### Request (POST /api/ventas/update)

```json
{
  "id_venta": 5,
  "nombre_cliente": "María González Pérez",
  "total_venta": 300.0,
  "descuento_total": 30.0,
  "id_usuario": 2,
  "detalle_venta": [
    {
      "cantidad": 3,
      "precio_unitario_repuesto": 100.0,
      "precio_sugerido_repuesto": 125.0,
      "id_repuesto": 5,
      "costo_repuesto": 80.0
    }
  ]
}
```

### Lo que sucede internamente:

1. **VentaController** recibe el DTO
2. **VentaMapper** convierte DTO → Domain (pasando id_usuario = 2)
3. **VentaService** ejecuta lógica de negocio de actualización
4. **VentaRepositoryAdapter** detecta que ya existe (id_venta = 5)
5. Se recupera la venta existente de BD
6. Se preservan campos originales:
   ```java
   created_at = "2025-12-09 14:30:15" // ¡NO CAMBIA!
   created_by = 1                      // ¡NO CAMBIA!
   ```
7. Se actualizan campos de modificación:
   ```java
   updated_at = "2025-12-09 16:45:22"
   updated_by = 2
   ```

### Registro en Base de Datos:

```sql
UPDATE venta SET
  nombre_cliente = 'María González Pérez',
  total = 300.00,
  descuento_total = 30.00,
  created_at = '2025-12-09 14:30:15',  -- PRESERVADO
  created_by = 1,                       -- PRESERVADO
  updated_at = '2025-12-09 16:45:22',   -- ACTUALIZADO
  updated_by = 2                        -- ACTUALIZADO
WHERE id_venta = 5;
```

---

## 3. Consultar Venta con Información de Auditoría

### Request (GET /api/ventas/findById/5)

### Response:

```json
{
  "idVenta": 5,
  "nombreCliente": "María González Pérez",
  "fechaVenta": "2025-12-09T14:30:15",
  "total": 300.00,
  "descuentoTotal": 30.00,
  "estadoVenta": true,
  "detalleVenta": [...]
}
```

**Nota:** Actualmente los campos de auditoría no se exponen en la respuesta. Si necesitas mostrarlos, puedes:

1. Agregar los campos al DTO de respuesta:

```java
@JsonProperty("created_at")
private LocalDateTime createdAt;

@JsonProperty("created_by")
private Integer createdBy;

@JsonProperty("updated_at")
private LocalDateTime updatedAt;

@JsonProperty("updated_by")
private Integer updatedBy;
```

2. Consultar directamente en la base de datos:

```sql
SELECT
  v.*,
  u1.username as creado_por_nombre,
  u2.username as actualizado_por_nombre
FROM venta v
LEFT JOIN usuario u1 ON v.created_by = u1.id_usuario
LEFT JOIN usuario u2 ON v.updated_by = u2.id_usuario
WHERE v.id_venta = 5;
```

---

## 4. Pruebas con cURL

### Crear Venta:

```bash
curl -X POST http://localhost:8080/api/ventas \
  -H "Content-Type: application/json" \
  -d '{
    "nombre_cliente": "Carlos Ruiz",
    "total_venta": 500.00,
    "descuento_total": 50.00,
    "id_usuario": 1,
    "detalle_venta": [
      {
        "cantidad": 5,
        "precio_unitario_repuesto": 100.00,
        "precio_sugerido_repuesto": 120.00,
        "id_repuesto": 3,
        "costo_repuesto": 75.00
      }
    ]
  }'
```

### Actualizar Venta:

```bash
curl -X POST http://localhost:8080/api/ventas/update \
  -H "Content-Type: application/json" \
  -d '{
    "id_venta": 10,
    "nombre_cliente": "Carlos Ruiz Modificado",
    "total_venta": 600.00,
    "descuento_total": 60.00,
    "id_usuario": 2,
    "detalle_venta": [
      {
        "cantidad": 6,
        "precio_unitario_repuesto": 100.00,
        "precio_sugerido_repuesto": 120.00,
        "id_repuesto": 3,
        "costo_repuesto": 75.00
      }
    ]
  }'
```

---

## 5. Pruebas con PowerShell

### Crear Venta:

```powershell
$body = @{
    nombre_cliente = "Ana Torres"
    total_venta = 350.00
    descuento_total = 35.00
    id_usuario = 1
    detalle_venta = @(
        @{
            cantidad = 3
            precio_unitario_repuesto = 100.00
            precio_sugerido_repuesto = 120.00
            id_repuesto = 7
            costo_repuesto = 80.00
        }
    )
} | ConvertTo-Json -Depth 10

Invoke-RestMethod -Uri "http://localhost:8080/api/ventas" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### Actualizar Venta:

```powershell
$body = @{
    id_venta = 12
    nombre_cliente = "Ana Torres Actualizada"
    total_venta = 400.00
    descuento_total = 40.00
    id_usuario = 2
    detalle_venta = @(
        @{
            cantidad = 4
            precio_unitario_repuesto = 100.00
            precio_sugerido_repuesto = 120.00
            id_repuesto = 7
            costo_repuesto = 80.00
        }
    )
} | ConvertTo-Json -Depth 10

Invoke-RestMethod -Uri "http://localhost:8080/api/ventas/update" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

---

## 6. Consultas SQL de Auditoría

### Ver historial completo de una venta:

```sql
SELECT
    v.id_venta,
    v.nombre_cliente,
    v.total,
    DATE_FORMAT(v.created_at, '%Y-%m-%d %H:%i:%s') as fecha_creacion,
    u1.username as creado_por,
    DATE_FORMAT(v.updated_at, '%Y-%m-%d %H:%i:%s') as fecha_actualizacion,
    u2.username as actualizado_por,
    TIMESTAMPDIFF(MINUTE, v.created_at, v.updated_at) as minutos_desde_creacion
FROM venta v
LEFT JOIN usuario u1 ON v.created_by = u1.id_usuario
LEFT JOIN usuario u2 ON v.updated_by = u2.id_usuario
WHERE v.id_venta = 5;
```

### Ventas nunca modificadas:

```sql
SELECT
    v.id_venta,
    v.nombre_cliente,
    v.total,
    v.created_at,
    u.username as creado_por
FROM venta v
LEFT JOIN usuario u ON v.created_by = u.id_usuario
WHERE v.updated_at IS NULL
ORDER BY v.created_at DESC;
```

### Ventas modificadas en las últimas 24 horas:

```sql
SELECT
    v.id_venta,
    v.nombre_cliente,
    v.updated_at,
    u.username as modificado_por
FROM venta v
LEFT JOIN usuario u ON v.updated_by = u.id_usuario
WHERE v.updated_at >= DATE_SUB(NOW(), INTERVAL 24 HOUR)
ORDER BY v.updated_at DESC;
```

### Actividad por usuario:

```sql
SELECT
    u.username,
    COUNT(DISTINCT v1.id_venta) as ventas_creadas,
    COUNT(DISTINCT v2.id_venta) as ventas_modificadas
FROM usuario u
LEFT JOIN venta v1 ON u.id_usuario = v1.created_by
LEFT JOIN venta v2 ON u.id_usuario = v2.updated_by
GROUP BY u.id_usuario, u.username
ORDER BY ventas_creadas DESC;
```

---

## 7. Escenarios de Error

### Error: No se envía id_usuario

**Request:**

```json
{
  "nombre_cliente": "Pedro López",
  "total_venta": 150.00,
  "descuento_total": 15.00,
  "detalle_venta": [...]
  // ¡Falta id_usuario!
}
```

**Resultado:**

- `created_by` será NULL en la base de datos
- Se recomienda agregar validación en el servicio

### Error: id_usuario inválido

**Request:**

```json
{
  "nombre_cliente": "Pedro López",
  "total_venta": 150.00,
  "id_usuario": 999,  // Usuario no existe
  "detalle_venta": [...]
}
```

**Resultado:**

- La venta se crea, pero created_by apunta a un usuario inexistente
- Se recomienda agregar validación para verificar que el usuario existe

---

## 8. Integración con Frontend (Angular)

### Servicio Angular:

```typescript
export class VentaService {
  private apiUrl = "http://localhost:8080/api/ventas";

  constructor(private http: HttpClient, private authService: AuthService) {}

  crearVenta(venta: VentaDTO): Observable<any> {
    // Obtener el usuario autenticado
    const usuario = this.authService.getCurrentUser();

    // Agregar el id_usuario al DTO
    const ventaConAuditoria = {
      ...venta,
      id_usuario: usuario.id_usuario,
    };

    return this.http.post(this.apiUrl, ventaConAuditoria);
  }

  actualizarVenta(venta: VentaDTO): Observable<any> {
    // Obtener el usuario autenticado
    const usuario = this.authService.getCurrentUser();

    // Agregar el id_usuario al DTO
    const ventaConAuditoria = {
      ...venta,
      id_usuario: usuario.id_usuario,
    };

    return this.http.post(`${this.apiUrl}/update`, ventaConAuditoria);
  }
}
```

### Componente Angular:

```typescript
export class VentaFormComponent {
  guardarVenta() {
    const ventaDTO = {
      nombre_cliente: this.form.value.nombreCliente,
      total_venta: this.form.value.total,
      descuento_total: this.form.value.descuento,
      detalle_venta: this.detalles,
      // NO incluir id_usuario aquí, el servicio lo agrega automáticamente
    };

    if (this.esEdicion) {
      ventaDTO.id_venta = this.ventaId;
      this.ventaService.actualizarVenta(ventaDTO).subscribe(
        (response) => console.log("Venta actualizada", response),
        (error) => console.error("Error", error)
      );
    } else {
      this.ventaService.crearVenta(ventaDTO).subscribe(
        (response) => console.log("Venta creada", response),
        (error) => console.error("Error", error)
      );
    }
  }
}
```

---

## Notas Importantes

1. **Siempre incluir id_usuario**: El frontend debe obtener el ID del usuario autenticado y enviarlo en cada request.

2. **No manipular fechas manualmente**: Las fechas de auditoría se generan automáticamente en el backend.

3. **Protección de datos**: Los campos `created_at` y `created_by` están protegidos contra modificaciones.

4. **Zona horaria**: Asegúrate de que el servidor tenga la zona horaria correcta configurada.
