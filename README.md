Club Náutico Valdivia - Backend

API REST desarrollada con Spring Boot para la gestión de usuarios, muelles, almacenes, servicios, boletas y documentos del Club Náutico Valdivia.

Tecnologías utilizadas

Java 21 y Spring Boot

Spring Data JPA y HATEOAS

PostgreSQL (Neon)

Neon Object Storage

Swagger

Maven

Lombok

Docker

Render

Arquitectura

El proyecto utiliza una arquitectura por capas:

Model: entidades y relaciones.

DTO: transferencia de datos.

Repository: acceso a la base de datos.

Service: lógica de negocio.

Controller: endpoints REST.

Config: configuraciones del sistema.

Ejecución local

Configurar las variables de entorno en el archivo .env y ejecutar:

docker compose up --build -d

Despliegue y documentación

El backend se encuentra desplegado en Render y conectado a Neon.

Backend en Render

Documentación Swagger

Proyecto académico

Institución: Duoc UC.

Asignatura: Fullstack II.

Año: 2026.

Proyecto en desarrollo con datos de prueba.
