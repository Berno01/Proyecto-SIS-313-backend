# Resumen del Módulo de Autenticación

## Archivos Creados

### 1. Domain Layer (Capa de Dominio)

#### Modelos

- `auth/domain/model/Usuario.java` - Entidad de dominio del usuario

#### Excepciones

- `auth/domain/exception/UsuarioNotFoundException.java` - Excepción cuando no se encuentra el usuario
- `auth/domain/exception/InvalidCredentialsException.java` - Excepción para credenciales inválidas

### 2. Application Layer (Capa de Aplicación)

#### Puertos de Entrada (Use Cases)

- `auth/application/port/in/LoginUseCase.java` - Interface para el caso de uso de login

#### Puertos de Salida (Persistence)

- `auth/application/port/out/UsuarioPersistencePort.java` - Interface para persistencia de usuarios

#### Servicios

- `auth/application/service/AuthService.java` - Servicio que implementa la lógica de autenticación con BCrypt

### 3. Infrastructure Layer (Capa de Infraestructura)

#### Adaptadores de Entrada - Web

**Controllers:**

- `auth/infrastructure/adapter/in/web/controller/AuthController.java` - Controller REST para el endpoint de login

**DTOs:**

- `auth/infrastructure/adapter/in/web/dto/LoginRequestDTO.java` - DTO para la petición de login
- `auth/infrastructure/adapter/in/web/dto/LoginResponseDTO.java` - DTO para la respuesta de login (sin password)

**Exception Handlers:**

- `auth/infrastructure/adapter/in/web/exception/AuthExceptionHandler.java` - Manejador global de excepciones

#### Adaptadores de Salida - Persistence

**Entities:**

- `auth/infrastructure/adapter/out/persistence/entity/UsuarioEntity.java` - Entidad JPA para la tabla usuario

**Repositories:**

- `auth/infrastructure/adapter/out/persistence/repository/UsuarioRepository.java` - Repositorio JPA

**Mappers:**

- `auth/infrastructure/adapter/out/persistence/mapper/UsuarioPersistenceMapper.java` - Mapper entre Entity y Domain (MapStruct)

**Adapters:**

- `auth/infrastructure/adapter/out/persistence/UsuarioRepositoryAdapter.java` - Adaptador que implementa el puerto de persistencia

#### Configuración

- `auth/infrastructure/config/SecurityConfig.java` - Bean de configuración para BCryptPasswordEncoder

### 4. Base de Datos

**SQL Migration:**

- Modificado: `src/main/resources/migrationsdb/tiendarepuestos.sql`
  - Agregada tabla `usuario`
  - Agregado INSERT del usuario admin (username: admin, password: admin123)

### 5. Dependencias

**Modificado:**

- `pom.xml` - Agregada dependencia de `spring-security-crypto` versión 6.2.1

### 6. Documentación y Utilidades

- `AUTH_MODULE_README.md` - Documentación completa del módulo
- `AUTH_TESTING_GUIDE.md` - Guía de pruebas con ejemplos en cURL, PowerShell, Postman y JavaScript
- `src/test/java/.../auth/PasswordHashGenerator.java` - Utilidad para generar hashes BCrypt

## Estructura Completa del Módulo

```
auth/
├── application/
│   ├── port/
│   │   ├── in/
│   │   │   └── LoginUseCase.java
│   │   └── out/
│   │       └── UsuarioPersistencePort.java
│   └── service/
│       └── AuthService.java
├── domain/
│   ├── exception/
│   │   ├── InvalidCredentialsException.java
│   │   └── UsuarioNotFoundException.java
│   └── model/
│       └── Usuario.java
└── infrastructure/
    ├── adapter/
    │   ├── in/
    │   │   └── web/
    │   │       ├── controller/
    │   │       │   └── AuthController.java
    │   │       ├── dto/
    │   │       │   ├── LoginRequestDTO.java
    │   │       │   └── LoginResponseDTO.java
    │   │       └── exception/
    │   │           └── AuthExceptionHandler.java
    │   └── out/
    │       └── persistence/
    │           ├── entity/
    │           │   └── UsuarioEntity.java
    │           ├── mapper/
    │           │   └── UsuarioPersistenceMapper.java
    │           ├── repository/
    │           │   └── UsuarioRepository.java
    │           └── UsuarioRepositoryAdapter.java
    └── config/
        └── SecurityConfig.java
```

## Características Implementadas

✅ Arquitectura Hexagonal (misma estructura que el módulo de venta)
✅ Controller recibe DTO y pasa al servicio
✅ Servicio mapea de DTO a dominio
✅ Servicio devuelve DTO (sin exponer contraseña)
✅ Encriptación BCrypt para contraseñas
✅ Usuario admin por defecto en la base de datos
✅ Manejo de excepciones personalizado
✅ Validación de credenciales
✅ Módulo completamente independiente
✅ Sin dependencias de otros módulos
✅ Sin configuración completa de Spring Security (solo BCrypt)

## Endpoint Disponible

**POST** `/api/auth/login`

Request:

```json
{
  "username": "admin",
  "password": "admin123"
}
```

Response (200 OK):

```json
{
  "id_usuario": 1,
  "username": "admin",
  "nombre_completo": "Administrador",
  "rol": "ADMIN"
}
```

## Próximos Pasos

Para usar este módulo:

1. Ejecutar el script SQL en la base de datos para crear la tabla `usuario`
2. Compilar el proyecto con Maven
3. Iniciar la aplicación Spring Boot
4. Probar el endpoint `/api/auth/login` con las credenciales del usuario admin

Para el frontend (Angular):

- Usar el endpoint para autenticar usuarios
- Guardar la respuesta en LocalStorage
- Crear guards para proteger rutas según el rol del usuario
