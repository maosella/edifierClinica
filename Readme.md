
# Certified Tech Developer

---

## Proyecto Integrador Back-End-1 Martin Osella
### Edifier Gestor de Turnos - Clinica Odontológica



El proyecto esta basado una aplicacion de estructura MVC realizada como ejercicio practico final para la materia de Back-End 1 de la carrera CTD. La
aplicacion se basa una API REST de gestión de turnos de una clínica odontológica a la que se identificó en este proyecto con la marca comercial EDIFIER.

Una vez que la aplicación esté corriendo, podés acceder a la url [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
y ver la documentación.

Se deja en la carpeta del proyecto la coleccion de postman para hacer las pruebas a la API, ya que no todas la funcionalidades estan disponibles desde las
vistas. Es necesario loguearse con el username "admin" y el password "admin" antes de empezar a hacer las solicitudes. También es posible loguearse con
username "user" y el password "user" para consultas mas acotadas.

## Pre-requisitos
-  [Maven](https://maven.apache.org/download.cgi)
-  [Java 11](https://www.oracle.com/java/technologies/downloads/#java11)

## Instalación
Por defecto Tomcat se levanta en el puerto 8080, si se quisiera configurar un puerto personalizado agregar `server.port=<PUERTO>`
en `/src/resources/application.properties`.

Una vez clonado el proyecto ejecutar el comando:
```bash
mvn clean package
java -jar ClinicaOdontologica.jar
```

---
###### Gracias a las personas que hicieron parte de este mini-proyecto y por darme la oportunidad de aprender de los que mas saben.

