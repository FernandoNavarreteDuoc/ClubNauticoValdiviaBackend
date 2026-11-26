# Club Náutico Valdivia - Backend

API REST desarrollada con Spring Boot para la gestión de usuarios, muelles, almacenes, servicios, boletas y documentos del Club Náutico Valdivia.

## Tecnologías utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- Spring HATEOAS
- PostgreSQL (Neon)
- Neon Object Storage
- Swagger
- Maven
- Lombok
- Docker
- Render

## Arquitectura

El proyecto utiliza una arquitectura por capas:

- **Model:** entidades y relaciones de la base de datos.
- **DTO:** transferencia de datos.
- **Repository:** acceso a la base de datos.
- **Service:** lógica de negocio.
- **Controller:** endpoints REST.
- **Config:** configuraciones del sistema.

## Clases del proyecto

| Model | DTO | Repository | Service | Controller |
|---|---|---|---|---|
| Usuario | UsuarioDTO | UsuarioRepository | UsuarioService | UsuarioController |
| Rol | RolDTO | RolRepository | RolService | RolController |
| Espacio | EspacioDTO | EspacioRepository | EspacioService | EspacioController |
| TipoEspacio | TipoEspacioDTO | TipoEspacioRepository | TipoEspacioService | TipoEspacioController |
| Servicio | ServicioDTO | ServicioRepository | ServicioService | ServicioController |
| Boleta | BoletaDTO | BoletaRepository | BoletaService | BoletaController |
| Detalle | DetalleDTO | DetalleRepository | DetalleService | DetalleController |
| TipoBoleta | TipoboletaDTO | TipoBoletaRepository | TipoBoletaService | TipoBoletaController |
| Metodopago | MetodopagoDTO | MetodopagoRepository | MetodopagoService | MetodopagoController |
| Documento | DocumentoDTO | DocumentoRepository | DocumentoService | DocumentoController |

### Clases adicionales

| Clase | Descripción |
|---|---|
| ClubnauticovaldiviaApplication | Inicio de la aplicación Spring Boot. |
| StorageConfig | Configuración de Neon Object Storage. |

## Base de datos

Base de datos PostgreSQL alojada en Neon, compuesta por 10 tablas relacionadas y 84 registros iniciales de prueba.

Los archivos se almacenan en Neon Object Storage y sus metadatos se registran en PostgreSQL.

## Ejecución local

Configurar las variables de entorno en el archivo `.env` y ejecutar:

```bash
docker compose up --build -d
```

Para detener el contenedor:

```bash
docker compose down
```

## Despliegue y documentación

El backend se encuentra desplegado en Render mediante Docker.

- [Backend en Render](https://clubnautico-backend.onrender.com)
- [Documentación Swagger](https://clubnautico-backend.onrender.com/swagger-ui/index.html)

## Proyecto académico

- **Institución:** Duoc UC.
- **Asignatura:** Fullstack II.
- **Semestre:** Cuarto semestre.
- **Año:** 2026.

Proyecto en desarrollo académico.
