# Estaciones de Servicio API

API REST para la gestión de estaciones de servicio. Permite crear, consultar, listar, actualizar y eliminar lógicamente estaciones de servicio.

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web MVC
- Spring Data JPA
- Spring Cache
- MySQL
- Maven
- JUnit 5
- Mockito

## Requisitos previos

Antes de ejecutar el proyecto, asegúrate de tener instalado:

- Java 17
- Maven, o usar el wrapper incluido `mvnw`
- MySQL
- Un cliente para probar APIs, por ejemplo Postman, Insomnia o cURL

## Configuración de base de datos

El proyecto usa MySQL como motor de base de datos.

Debes crear una base de datos llamada base_estaciones_de_servicio:

> CREATE DATABASE base_estaciones_de_servicio;

Asegúrate de tener las credenciales de acceso correctas y ajustar la configuración de la base de datos en el archivo `application.properties`

Ejemplo de configuración:

properties spring.datasource.url=jdbc:mysql://localhost:3306/base_estaciones_de_servicio 
spring.datasource.username=<DB_USERNAME> 
spring.datasource.password=<DB_PASSWORD>
spring.jpa.hibernate.ddl-auto=update spring.jpa.show-sql=true spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

## Instrucciones para correr el proyecto

### 1. Clonar el repositorio
> git clone https://github.com/santiagoacostaf/EstacionesDeServicio.git

> cd EstacionesDeServicio
### 2. Desde tu gestor de base de datos, ejecutar
> CREATE DATABASE base_estaciones_de_servicio;
### 3. Ajustar las credenciales de acceso en el archivo `application.properties`
### 4. Compilar el proyecto
En windows:
Desde la consola, o el power shell ejecutar:
> .\mvnw clean spring-boot:run
 
En linux:
Desde la consola, ejecutar:
> ./mvnw clean spring-boot:run

## Decisiones técnicas y arquitectura

1. El proyecto está organizado en una arquitectura de capas, con un modelo de dominio, servicios y controladores separados.
2. Se utiliza Spring Boot para la creación de la aplicación web, lo que facilita la configuración y el despliegue.
3. La base de datos se modela con JPA (Java Persistence API) y Hibernate como proveedor ORM, lo que permite una fácil integración con MySQL.
4. Se utiliza Maven como gestor de dependencias y construcción, lo que facilita la gestión de las librerías y el proceso de compilación.
5. El controlador (EstacionDeServicioController) depende de la interfaz EstacionDeServicioService, para cumplir con el principio de inversión de dependencias y seguir el patrón de diseño SOLID.
6. Para la eliminación de registros, se utiliza un borrado lógico que marca los registros como  "INACTIVO" en lugar de eliminarlos físicamente, lo que mejora la integridad de los datos y facilita la trazabilidad de información en caso de necesidad.

## Supuestos realizados

- Se asume que cada estación de servicio tiene un código único.
- Se asume que la base de datos MySQL ya existe antes de ejecutar la aplicación.
- Se asume que al crear una estación, esta debe quedar como `ACTIVA`.
- Se asume que eliminar una estación no significa borrarla físicamente, sino cambiar su estado a `INACTIVA`.
- Se asume que las coordenadas enviadas en el request son valores válidos y convertibles a número decimal.
- Se asume que el consumidor de la API enviará y recibirá información en formato JSON.
- Se asume que los errores por datos duplicados, como un código repetido, deben tratarse como conflictos de datos.

## Ejemplos para probar los endpoints del crud:

Se va a facilitar una collecciónde postman para probar los endpoints del crud. sin embargo a continuación listaré
unos ejemplos de como se puede probar cada endpoint en linux.

### Creación:

curl -X POST http://localhost:8080/api/stations   -H "Content-Type: application/json"   -d '{
"codigo": "EDS-001",
"nombre": "Estación Principal",
"direccion": "Calle 123 #45-67",
"ciudad": "Bogotá",
"latitud": "4.7110",
"longitud": "-74.0721",
"estado": "ACTIVA"
}'

### Consulta:
curl -i -X GET http://localhost:8080/api/stations/1

### Listado:
curl -i -X GET http://localhost:8080/api/stations

### Actualización:
curl -X PUT http://localhost:8080/api/stations/1   -H "Content-Type: application/json"   -d '{
"nombre": "Estación Principal",
"direccion": "Calle 123 #45-67",
"ciudad": "Bogotá",
"latitud": "4.7110",
"longitud": "-74.0721",
"estado": "ACTIVA"
}'

### Eliminación:
curl -X DELETE http://localhost:8080/api/stations/1

## Ejecución de tests de unidad

Se desarrollaron tests de unidad para los métodos de la clase EstacionDeServicioService.

En windows:
Desde la consola, o el power shell ejecutar:
> .\mvnw test

En linux:
Desde la consola, ejecutar:
> ./mvnw test
