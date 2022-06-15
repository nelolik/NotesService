# NotesService

![logo](notes.jpg)

This application allows authorized users to save text records, edit and delete them.

The project was created to try out the creation of an MVC application with Spring Boot that works with databases and
http.

## Building and Testing

Чтобы собрать приложение нужно выполнить команду После выполнения сборки jar файл будет находиться в папке `/target`.

NotesService uses [Maven](https://maven.apache.org/) and requires `Java 17` for proper building and proper operations.
To build, execute unit tests and package run:

```shell
mvn clean install
```

The zip distribution can be found in the directory `/target`

## Running application

Before starting, you need to raise the PostgreSQL database with the parameters specified in the file
application.properties

To run the application go to `/target` directory and run the following command:

```shell
java -jar SpringWebMVC-0.0.1-SNAPSHOT.jar
```