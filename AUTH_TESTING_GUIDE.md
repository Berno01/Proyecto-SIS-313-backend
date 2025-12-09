# Pruebas del Módulo de Autenticación

## Usando cURL

### Login Exitoso (usuario admin)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

**Respuesta esperada:**

```json
{
  "id_usuario": 1,
  "username": "admin",
  "nombre_completo": "Administrador",
  "rol": "ADMIN"
}
```

### Login con credenciales incorrectas

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"wrongpassword\"}"
```

**Respuesta esperada (401 Unauthorized):**

```json
{
  "error": "Credenciales inválidas",
  "mensaje": "Credenciales inválidas"
}
```

### Login con usuario inexistente

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"noexiste\",\"password\":\"admin123\"}"
```

**Respuesta esperada (404 Not Found):**

```json
{
  "error": "Usuario no encontrado",
  "mensaje": "Usuario no encontrado: noexiste"
}
```

## Usando PowerShell (Windows)

### Login Exitoso

```powershell
$body = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

### Login con credenciales incorrectas

```powershell
$body = @{
    username = "admin"
    password = "wrongpassword"
} | ConvertTo-Json

try {
    Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" `
        -Method POST `
        -ContentType "application/json" `
        -Body $body
} catch {
    $_.ErrorDetails.Message | ConvertFrom-Json
}
```

## Usando Postman

1. **Crear nueva request:**

   - Method: `POST`
   - URL: `http://localhost:8080/api/auth/login`

2. **Headers:**

   ```
   Content-Type: application/json
   ```

3. **Body (raw JSON):**
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```

## Prueba desde JavaScript (Frontend)

```javascript
async function login(username, password) {
  try {
    const response = await fetch("http://localhost:8080/api/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    });

    if (!response.ok) {
      const error = await response.json();
      throw new Error(error.mensaje);
    }

    const data = await response.json();
    console.log("Login exitoso:", data);

    // Guardar en localStorage
    localStorage.setItem("usuario", JSON.stringify(data));

    return data;
  } catch (error) {
    console.error("Error en login:", error);
    throw error;
  }
}

// Uso:
login("admin", "admin123")
  .then((usuario) => console.log("Usuario logueado:", usuario))
  .catch((error) => console.error("Error:", error));
```

## Notas Importantes

1. **Antes de probar**, asegúrate de:

   - Ejecutar el script SQL para crear la tabla `usuario`
   - Insertar el usuario admin
   - Iniciar la aplicación Spring Boot

2. **Puerto del servidor**: El puerto por defecto es 8080. Si usas otro puerto, modifica las URLs.

3. **CORS**: Si pruebas desde un frontend en diferente origen, necesitarás configurar CORS en Spring Boot.

4. **Seguridad en producción**:
   - Cambia la contraseña del usuario admin
   - Considera implementar JWT para sesiones
   - Agrega rate limiting para prevenir ataques de fuerza bruta
