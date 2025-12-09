# Documentación de Endpoints Auxiliares

## 📋 Resumen

Este documento describe los endpoints auxiliares para gestionar las entidades relacionadas: **Vehículo**, **Sistema Auto** y **Categoría**. Estos endpoints permiten alimentar los selectores del frontend y crear entidades "al vuelo" desde modales.

---

## 🚗 VehiculoController

**Base URL:** `/api/vehiculos`

### 1. Listar Todos los Vehículos

**Endpoint:** `GET /api/vehiculos/findAll`

**Descripción:** Devuelve la lista completa de vehículos registrados.

**Request:** No requiere parámetros

**Response:**

```json
{
  "status": true,
  "message": "Lista de vehículos obtenida exitosamente",
  "data": [
    {
      "id": 1,
      "marca": "TOYOTA",
      "modelo": "COROLLA"
    },
    {
      "id": 2,
      "marca": "HONDA",
      "modelo": "CIVIC"
    }
  ]
}
```

---

### 2. Crear Vehículo

**Endpoint:** `POST /api/vehiculos/create`

**Descripción:** Crea un nuevo vehículo o devuelve uno existente si ya está registrado.

**Lógica de Negocio:**

- ✅ **Normalización automática:** Convierte marca y modelo a MAYÚSCULAS y aplica `.trim()`
- ✅ **Prevención de duplicados:** Si la combinación marca/modelo ya existe, devuelve el registro existente
- ✅ **Sin errores:** No genera error si el vehículo ya existe, simplemente lo retorna

**Request Body:**

```json
{
  "marca": "Toyota",
  "modelo": "Corolla"
}
```

**Response (nuevo):**

```json
{
  "status": true,
  "message": "Vehículo creado/encontrado exitosamente",
  "data": {
    "id": 1,
    "marca": "TOYOTA",
    "modelo": "COROLLA"
  }
}
```

**Response (existente):**

```json
{
  "status": true,
  "message": "Vehículo creado/encontrado exitosamente",
  "data": {
    "id": 1,
    "marca": "TOYOTA",
    "modelo": "COROLLA"
  }
}
```

**Validaciones:**

- `marca`: Obligatorio, no puede estar vacío
- `modelo`: Obligatorio, no puede estar vacío

**Ejemplo de Error:**

```json
{
  "status": false,
  "message": "Error de validación",
  "errors": {
    "marca": "La marca es obligatoria"
  }
}
```

---

## ⚙️ SistemaAutoController

**Base URL:** `/api/sistemas`

### 1. Listar Todos los Sistemas

**Endpoint:** `GET /api/sistemas/findAll`

**Descripción:** Devuelve la lista completa de sistemas automotrices registrados.

**Request:** No requiere parámetros

**Response:**

```json
{
  "status": true,
  "message": "Lista de sistemas obtenida exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "FRENOS"
    },
    {
      "id": 2,
      "nombre": "SUSPENSIÓN"
    },
    {
      "id": 3,
      "nombre": "MOTOR"
    }
  ]
}
```

---

### 2. Crear Sistema

**Endpoint:** `POST /api/sistemas/create`

**Descripción:** Crea un nuevo sistema automotriz o devuelve uno existente si ya está registrado.

**Lógica de Negocio:**

- ✅ **Normalización automática:** Convierte nombre_sistema a MAYÚSCULAS y aplica `.trim()`
- ✅ **Prevención de duplicados:** Si el nombre ya existe, devuelve el registro existente
- ✅ **Sin errores:** No genera error si el sistema ya existe, simplemente lo retorna

**Request Body:**

```json
{
  "nombre_sistema": "Frenos"
}
```

**Response (nuevo):**

```json
{
  "status": true,
  "message": "Sistema creado/encontrado exitosamente",
  "data": {
    "id": 1,
    "nombre": "FRENOS"
  }
}
```

**Response (existente):**

```json
{
  "status": true,
  "message": "Sistema creado/encontrado exitosamente",
  "data": {
    "id": 1,
    "nombre": "FRENOS"
  }
}
```

**Validaciones:**

- `nombre_sistema`: Obligatorio, no puede estar vacío

**Ejemplo de Error:**

```json
{
  "status": false,
  "message": "Error de validación",
  "errors": {
    "nombre_sistema": "El nombre del sistema es obligatorio"
  }
}
```

---

## 🏷️ CategoriaAuxiliarController

**Base URL:** `/api/categorias`

### 1. Listar Todas las Categorías

**Endpoint:** `GET /api/categorias/findAll`

**Descripción:** Devuelve la lista de categorías activas (solo categorías con `estadoCategoria = true`).

**Request:** No requiere parámetros

**Response:**

```json
{
  "status": true,
  "message": "Lista de categorías obtenida exitosamente",
  "data": [
    {
      "id": 1,
      "nombre": "ACEITES"
    },
    {
      "id": 2,
      "nombre": "FILTROS"
    },
    {
      "id": 3,
      "nombre": "BUJÍAS"
    }
  ]
}
```

---

### 2. Crear Categoría

**Endpoint:** `POST /api/categorias/create`

**Descripción:** Crea una nueva categoría o devuelve una existente si ya está registrada.

**Lógica de Negocio:**

- ✅ **Normalización automática:** Convierte nombre_categoria a MAYÚSCULAS y aplica `.trim()`
- ✅ **Prevención de duplicados:** Si el nombre ya existe, devuelve el registro existente
- ✅ **Estado automático:** Las nuevas categorías se crean con `estadoCategoria = true`
- ✅ **Sin errores:** No genera error si la categoría ya existe, simplemente la retorna

**Request Body:**

```json
{
  "nombre_categoria": "Aceites"
}
```

**Response (nuevo):**

```json
{
  "status": true,
  "message": "Categoría creada/encontrada exitosamente",
  "data": {
    "id": 1,
    "nombre": "ACEITES"
  }
}
```

**Response (existente):**

```json
{
  "status": true,
  "message": "Categoría creada/encontrada exitosamente",
  "data": {
    "id": 1,
    "nombre": "ACEITES"
  }
}
```

**Validaciones:**

- `nombre_categoria`: Obligatorio, no puede estar vacío

**Ejemplo de Error:**

```json
{
  "status": false,
  "message": "Error de validación",
  "errors": {
    "nombre_categoria": "El nombre de la categoría es obligatorio"
  }
}
```

---

## 🔧 Características Técnicas

### Normalización de Texto

Todos los endpoints de creación aplican:

1. **`.trim()`:** Elimina espacios en blanco al inicio y final
2. **`.toUpperCase()`:** Convierte todo el texto a mayúsculas

**Ejemplos:**

- `" toyota "` → `"TOYOTA"`
- `"  frenos  "` → `"FRENOS"`
- `"aceites"` → `"ACEITES"`

### Prevención de Duplicados

Antes de crear un nuevo registro, el sistema:

1. Normaliza el texto de entrada
2. Busca en la base de datos si ya existe
3. Si existe: Devuelve el registro existente (código 201)
4. Si no existe: Crea el nuevo registro (código 201)

### Arquitectura

- **Controllers:** Capa de presentación con validación de entrada
- **Services:** Lógica de negocio con normalización y verificación
- **Repositories:** Acceso a datos con queries personalizadas

---

## 📊 Diagrama de Flujo - Creación con Normalización

```
┌─────────────────┐
│  POST /create   │
└────────┬────────┘
         │
         ▼
┌─────────────────────┐
│ Validar Request DTO │
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│ Normalizar Texto    │
│ (.trim() + upper()) │
└────────┬────────────┘
         │
         ▼
┌─────────────────────┐
│ ¿Existe en DB?      │
└────────┬────────────┘
         │
    ┌────┴────┐
    │         │
   SÍ        NO
    │         │
    ▼         ▼
┌───────┐ ┌───────────┐
│Return │ │  Crear    │
│Existe │ │  Nuevo    │
└───────┘ └─────┬─────┘
                │
                ▼
         ┌──────────────┐
         │ Return Nuevo │
         └──────────────┘
```

---

## 🎯 Casos de Uso

### Uso desde Frontend - Selector de Vehículos

```javascript
// 1. Cargar opciones del selector
async function loadVehiculos() {
  const response = await fetch("/api/vehiculos/findAll");
  const data = await response.json();
  // Llenar <select> con data.data
}

// 2. Crear vehículo "al vuelo" desde modal
async function createVehiculo(marca, modelo) {
  const response = await fetch("/api/vehiculos/create", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ marca, modelo }),
  });
  const data = await response.json();
  return data.data.id; // Usar este ID en el formulario principal
}
```

### Uso desde Frontend - Selector de Sistemas

```javascript
// 1. Cargar opciones del selector
async function loadSistemas() {
  const response = await fetch("/api/sistemas/findAll");
  const data = await response.json();
  // Llenar <select> con data.data
}

// 2. Crear sistema "al vuelo" desde modal
async function createSistema(nombreSistema) {
  const response = await fetch("/api/sistemas/create", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nombre_sistema: nombreSistema }),
  });
  const data = await response.json();
  return data.data.id;
}
```

### Uso desde Frontend - Selector de Categorías

```javascript
// 1. Cargar opciones del selector (solo activas)
async function loadCategorias() {
  const response = await fetch("/api/categorias/findAll");
  const data = await response.json();
  // Llenar <select> con data.data
}

// 2. Crear categoría "al vuelo" desde modal
async function createCategoria(nombreCategoria) {
  const response = await fetch("/api/categorias/create", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nombre_categoria: nombreCategoria }),
  });
  const data = await response.json();
  return data.data.id;
}
```

---

## 🗄️ Estructura de Base de Datos

### Tabla: vehiculo

```sql
CREATE TABLE vehiculo (
  id_vehiculo INT PRIMARY KEY AUTO_INCREMENT,
  marca VARCHAR(50) NOT NULL,
  modelo VARCHAR(50) NOT NULL,
  UNIQUE KEY unique_marca_modelo (marca, modelo)
);
```

### Tabla: sistema_auto

```sql
CREATE TABLE sistema_auto (
  id_sistema INT PRIMARY KEY AUTO_INCREMENT,
  nombre_sistema VARCHAR(100) NOT NULL,
  UNIQUE KEY unique_nombre_sistema (nombre_sistema)
);
```

### Tabla: categoria

```sql
CREATE TABLE categoria (
  idCategoria INT PRIMARY KEY AUTO_INCREMENT,
  nombre_categoria VARCHAR(100) NOT NULL,
  estadoCategoria BOOLEAN DEFAULT TRUE,
  UNIQUE KEY unique_nombre_categoria (nombre_categoria)
);
```

---

## ⚠️ Notas Importantes

1. **Normalización Obligatoria:** Todo texto se almacena en MAYÚSCULAS para consistencia
2. **No Hay Duplicados:** La lógica previene duplicados automáticamente
3. **Categorías Activas:** Solo se listan categorías con `estadoCategoria = true`
4. **CORS Habilitado:** Todos los endpoints permiten `origins = "*"`
5. **Validación Jakarta:** Se usa `@Valid` para validación automática de DTOs

---

## 🔍 Testing con cURL

### Crear Vehículo

```bash
curl -X POST http://localhost:8080/api/vehiculos/create \
  -H "Content-Type: application/json" \
  -d '{"marca":"toyota","modelo":"corolla"}'
```

### Listar Vehículos

```bash
curl http://localhost:8080/api/vehiculos/findAll
```

### Crear Sistema

```bash
curl -X POST http://localhost:8080/api/sistemas/create \
  -H "Content-Type: application/json" \
  -d '{"nombre_sistema":"frenos"}'
```

### Listar Sistemas

```bash
curl http://localhost:8080/api/sistemas/findAll
```

### Crear Categoría

```bash
curl -X POST http://localhost:8080/api/categorias/create \
  -H "Content-Type: application/json" \
  -d '{"nombre_categoria":"aceites"}'
```

### Listar Categorías

```bash
curl http://localhost:8080/api/categorias/findAll
```

---

## 📦 Archivos Creados

### DTOs

- `VehiculoRequestDTO.java` - Request para crear vehículo
- `VehiculoResponseDTO.java` - Response con datos de vehículo
- `SistemaAutoRequestDTO.java` - Request para crear sistema
- `SistemaAutoResponseDTO.java` - Response con datos de sistema
- `CategoriaRequestDTO.java` - Request para crear categoría
- `CategoriaResponseDTO.java` - Response con datos de categoría

### Services

- `VehiculoService.java` - Lógica de negocio de vehículos
- `SistemaAutoService.java` - Lógica de negocio de sistemas
- `CategoriaAuxiliarService.java` - Lógica de negocio de categorías

### Controllers

- `VehiculoController.java` - Endpoints REST de vehículos
- `SistemaAutoController.java` - Endpoints REST de sistemas
- `CategoriaAuxiliarController.java` - Endpoints REST de categorías

### Repositories (Modificados)

- `VehiculoRepository.java` - Agregado `findByMarcaAndModelo()`
- `SistemaAutoRepository.java` - Agregado `findByNombreSistema()`
- `CategoriaRepository.java` - Agregado `findByNombre_categoria()`
