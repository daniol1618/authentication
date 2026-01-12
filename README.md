# Users Service - Servicio de Autenticación de Usuarios

## Índice General

1. [Descripción General](#descripción-general)
2. [Requisitos](#requisitos)
3. [Construcción del Proyecto](#construcción-del-proyecto)
4. [Ejecución del Proyecto](#ejecución-del-proyecto)
5. [Ejecución de Pruebas Unitarias](#ejecución-de-pruebas-unitarias)
6. [Endpoints Disponibles en Formato CURL para copiar y pegar en terminal](#endpoints-disponibles-en-formato-curl-para-copiar-y-pegar-en-terminal)
   - [Crear Usuario](#crear-usuario)
   - [Login (requiere token previo)](#login-requiere-token-previo)
   - [Crear Usuario con Contraseña Inválida](#crear-usuario-contrasena-invalida)
   - [Crear Usuario Duplicado (Email ya existente)](#crear-usuario-duplicado-email-ya-existente)
   - [Consultar Usuario por Id](#consultar-usuario-por-id)
   - [Consultar Todos los Usuarios](#consultar-todos-los-usuarios)
7. [Formato Estándar de Errores](#formato-estándar-de-errores)


## Descripción General
Microservicio desarrollado con **Spring Boot 2.5.14** y **Gradle 7.4** para la creación, consulta y autenticación de usuarios.

El servicio permite:

- Crear usuarios (`POST /sign-up`)
- Consultar usuarios (`GET /sign-up`, `GET /sign-up/{id}`)
- Autenticarse mediante token (`POST /login`)
- Persistir datos en base de datos en memoria **H2**
- Generar y renovar tokens **JWT**
- Encriptar contraseñas con **BCrypt**
- Manejar errores con un contrato estándar

---

## Requisitos

- Java 1.8.0_202
- Gradle 7.4

Verificar instalación:

```bash
java -version
gradle -v
```

---

## Construcción del proyecto

Desde la raíz del proyecto ejecutar:

### En Linux / Mac
```bash
./gradlew clean build
```

### En Windows
```bash
gradlew.bat clean build
```

Esto descargará dependencias, compilará el proyecto y ejecutará los tests.

---

## Ejecución del proyecto

### En Linux / Mac
```bash
./gradlew bootRun
```

### En Windows
```bash
gradlew.bat bootRun
```

La aplicación se levantará en:

```
http://localhost:8080
```
## Ejecución de pruebas unitarias

El proyecto incluye pruebas unitarias para la capa **Service** utilizando **JUnit 5** y **Mockito**.

Para ejecutar todas las pruebas:

### En Linux / Mac
```bash
./gradlew test
```

### En Windows
```bash
gradlew.bat test
```

Gradle compilará el proyecto y ejecutará todas las pruebas unitarias.

Si alguna prueba falla, el proceso terminará con error y se mostrará el detalle en consola.

---

## Endpoints disponibles en Formato CURL para copiar y pegar en terminal

### Crear usuario

```bash
curl --location 'http://localhost:8080/sign-up' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "juan2@mail.com",
    "password": "a2asfGfdfdf4",
    "name": "Juan Perez",
    "phones": [
      {
        "number": 1234567,
        "cityCode": 1,
        "countryCode": "57"
      }
    ]
}'
```

---

### Login (requiere token previo)

```bash
curl --location --request POST 'http://localhost:8080/login' \
--header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqdWFuMkBtYWlsLmNvbSIsImlhdCI6MTc2ODI1NzU2NiwiZXhwIjoxNzY4MjYxMTY2fQ.Aw0KDoQbxjQxLknhXGsIZelx4O9AaTnd3gZkkDvoG5ffQR5QpyKtJUEEq8QXne84dZvlpWlyUsVuDqREwOPl_w'
```

Devuelve toda la información del usuario y un nuevo token.

---

### Crear Usuario Contrasena Invalida
```bash
curl --location 'http://localhost:8080/sign-up' \
--header 'Content-Type: application/json' \
--data-raw '{
"email": "ana@mail.com",
"password": "abcdef",
"name": "Ana"
}'
```

### Crear Usuario Duplicado (Email ya existente)
```bash
curl --location 'http://localhost:8080/sign-up' \
--header 'Content-Type: application/json' \
--data-raw '{
"email": "juan2@mail.com",
"password": "a2asfGf4",
"name": "Juan Duplicado"
}'
```
### Consultar usuario por Id
```bash
curl --location 'http://localhost:8080/sign-up/930d2a94-5356-4c4f-a7dc-44dd9c11858b'
```

### Consultar todos los usuarios
```bash
curl --location 'http://localhost:8080/sign-up'
```

---

## Formato estándar de errores

Todos los errores siguen este formato:

```json
{
  "error": [
    {
      "timestamp": "2026-01-11T12:00:00",
      "codigo": 400,
      "detail": "Mensaje de error"
    }
  ]
}
```