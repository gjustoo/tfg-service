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




# WEEK 3


## PROGRESS

### DONE
#### RESEARCH

He buscado informacion sobre la politica de procesamiento de los datos obtenidos através de las APIs de las plataformas que voy a utilizar, en un primer momento estas plataformas eran las siguientes:

* Twitter
* Instagram
* Reddit

Al indagar sobre las políticas de uso de las APIs he encontrado que la API de Instagram restringe el uso de su contenido en plataformas de terceros  y promueve el uso de "embeddeds" lo cual no me sirve para mi proyecto.

>"No usar la Plataforma de Instagram para simplemente publicar contenido de Usuario [...]"

![instagram policy](https://i.imgur.com/O4QB1IT.jpg)

Como plataforma alternativa he optado por Pinterest, ya que tiene una API bien documentada y *fomenta* el uso de su API para la creación de este tipo de plataformas: 

![pinterest policy](https://i.imgur.com/JJ4lvJk.jpg)
![pinterest policy 2](https://i.imgur.com/vSGULw2.jpg)

[Pinterest policy documentation](tab:https://policy.pinterest.com/en/developer-guidelines)

Las politicas de uso del resto de plataformas:

Twitter:

![Twitter policy](https://i.imgur.com/7u9MSaz.jpg)

[Twitter policy documentation](tab:https://developer.twitter.com/en/developer-terms/policy#3-b)

Reddit:

![Reddit policy](https://i.imgur.com/udRPcob.jpg)

[Reddit policy documentation](tab:https://www.reddit.com/wiki/api-terms#wiki_2.__your_use_of_reddit_apis)

#### Conclusion

Al final he optado por usar Twitter, Reddit y Pinterest, ya que sus politicas de privacidad y uso cumplen con mis necesidades.

### THIS WEEK 

* Diseñar y desarrollar un Backend Basico.


Gracias por leer el update.

:)



# WEEK 4

## PROGRESS

### DONE

#### IMPLEMENTATION

He trasteado con la autenticacion de Twitter, atraves de un "wrapper", es una libreria que engloba a la API de Twitter.
El resultado ha sido que, usando Twitter como metodo de autenticacion, complica mucho las cosas, además el acceso a la API por defecto no te permite el uso del servicio de Auth.

Finalmente he optado por gestionar yo mismo la autenticacion, Spring Security tiene herramientas para encriptar y desencriptar contraseñas, usaré esa libreria para la gestion de contraseñas, se encriptarán usando BCrypt.


Me he creado una clase de utilidad para la gestion de Contraseñas.

Me he creado clases genericas para una implementacion mas limpia y correcta.

He creado la entidad Usuario con sus respectivas clases de Servicio y Repositorio.



Gracias por leer el update.

:)