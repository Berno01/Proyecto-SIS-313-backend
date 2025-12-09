# Módulo de Autenticación (Auth)

## Estructura del Módulo

El módulo `auth` sigue la arquitectura hexagonal utilizada en el proyecto, con las siguientes capas:

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

## Endpoint de Login

### POST `/api/auth/login`

**Request Body:**

```json
{
  "username": "admin",
  "password": "admin123"
}
```

**Response exitosa (200 OK):**

```json
{
  "id_usuario": 1,
  "username": "admin",
  "nombre_completo": "Administrador",
  "rol": "ADMIN"
}
```

**Response con credenciales inválidas (401 UNAUTHORIZED):**

```json
{
  "error": "Credenciales inválidas",
  "mensaje": "Credenciales inválidas"
}
```

**Response con usuario no encontrado (404 NOT FOUND):**

```json
{
  "error": "Usuario no encontrado",
  "mensaje": "Usuario no encontrado: <username>"
}
```

## Base de Datos

### Tabla `usuario`

```sql
CREATE TABLE `tiendarepuestos`.`usuario`
(
    id_usuario INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre_completo VARCHAR(100) NOT NULL,
    rol VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY `idx_username_unique` (username)
)
```

### Usuario por Defecto

- **Username:** `admin`
- **Password:** `admin123`
- **Nombre Completo:** Administrador
- **Rol:** ADMIN

## Seguridad

El módulo utiliza **BCryptPasswordEncoder** para el hash de contraseñas:

- Las contraseñas se almacenan hasheadas en la base de datos
- La verificación se realiza con `passwordEncoder.matches(passwordRaw, passwordHash)`
- **NO** se implementa Spring Security completo (sin WebSecurityConfigurerAdapter)
- Solo se usa la utilidad de encriptación de forma manual en el servicio

## Flujo de Autenticación

1. **Controller** recibe el `LoginRequestDTO` con username y password
2. **Controller** llama al servicio pasando los datos
3. **Service** mapea los datos y busca el usuario en la base de datos
4. **Service** verifica la contraseña usando BCrypt
5. **Service** devuelve un `LoginResponseDTO` sin la contraseña
6. **Controller** retorna la respuesta al cliente

## Configuración

La configuración de BCrypt se encuentra en `SecurityConfig.java`:

```java
@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

## Notas Importantes

- Las contraseñas **NUNCA** se exponen en las respuestas
- El servicio maneja las excepciones de forma específica
- El módulo es completamente independiente de otros módulos
- Sigue el mismo patrón de arquitectura hexagonal del módulo de ventas
