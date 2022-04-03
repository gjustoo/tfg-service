# BACKEND PROYECTO FINAL DE GRADO


### WEEK 1

Esta semana he pensado en la estructura del proyecto y en el Stack Tecnologico que voy a untilizar.
    Este Stack estará compuesto por:
    * **BASE DE DATOS** : PostgresSQL
    * **BACKEND SERVICE** : SpringBoot (JAVA)
    * **FRONTEND** : React
Mi intencion es poder desplegar todos los servicios en Docker, pero por ahora solo voy a tener la BBDD, el resto de servicios los desplegare cuando esten acabados.



# WEEK 2

He creado un contenedor Docker con **Postgres** instalado, para usarlo como base de datos del proyecto :

```shell
$ docker run --name tfg_database  -e POSTGRES_PASSWORD=postgres -d --restart always -p 5432:5432 postgres

```

Despues he instalado y comprobado que las variables de java funcionaban correctamente.

Desde la pagina de [SpringInitialzr](tab:https://start.spring.io/) me he creado un proyecto SpringBoot en Maven.
Las dependencias que me he puesto son estas :
```xml
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>

		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.projectlombok</groupId>
			<artifactId>lombok</artifactId>
			<optional>true</optional>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
```

* **JPA** : Para comunicacion entre BBDD y java.
* **starter-web** : Permite la creacion de API REST
* **Lombok** : Elimina el codigo "Boilerplate" sustituyendolo con etiquetas 
* **Driver Postgres** : Para la conexion a Postgres

Tambien he creado un repositorio GitHub sobre el cual trabajaré:
* [TFG-SERVICE](tab:https://github.com/gjustoo/tfg)

Y eso es todo lo que he podido hacer esta semana 2.

### NEXT WEEK 

La semana siguiente quiero tener desarrollado un Backend Basico, con las entidades creadas capaz de hacer operaciones crud basicas, y algun tipo de funcionalidad mas especifica de cara al proyecto final.


