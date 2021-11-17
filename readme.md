# Jira Like Board Coding Challenge

- Java version: 11
- Database: Postgresql
- Database schema management: liquibase
- Todo service url: http://localhost:8081
- User service url: http://localhost:8081
- Testing framework: Mockito, Junit
- OpenAPI specification (openapi.json) enclosed in the folder
- Postman collection (app.postman_collection.json) enclosed in the folder
- There are also some unit tests to test the functionality of service classes inside src/test/java/

# Running the application in dev mode
Before running the application, a postgres instance should be up and running. The following docker command pulls and runs a postgres image with a default username "postgres", password "admin" and the with a database named "leanix"
```shell script
docker run --name leanix -e POSTGRES_PASSWORD=admin -e POSTGRES_DB=leanix -d -p 5432:5432 postgres
```
The application can be run in dev mode on command line (in the directory where pom.xml exists) with the following command

```shell script
mvn spring-boot:run
```


There is also a Dockerfile to start the application in a container. To build the image and run it navigate through todo/user project and
```shell script
docker build -t todo . 
docker run -p 8081:8081 todo

docker build -t user . 
docker run -p 8081:8081 user
```  
commands can be used.



# Implementation Details

- All the endpoints can be found in UserController.java and TodoController.java
- The database tables are automatically created during startup by liquibase (changelog file can be found at src/main/resources/db/changelog)
- Name of the database files are board, customer and task
- API request and response model classes can be found under "model package". 

