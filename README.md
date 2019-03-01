# movierama-application

## Overview

The application is a social sharing platform where users can share their favorite movies and vote them. The creators of the mocies cannot vote for their movies.

## Prerequisites
- JDK 1.8 (Oracle)
- Maven 3
- npm 6.4.1

## Information about the deployment

The project is splited into two modules. The server side ( movierama ) and client side ( movierama-client ). In order to have a successful running environment you have to start first the server side and then the client side. They both work separately but the client side does not have appropriate details for the happy path like user details, movie details and user's opinions.
Also in this project it is used an in-memory H2 database provided by Spring in order to do not have to do the installation of external databases. Also there are three ( 3 ) created tables in this DB.

Users: Keeps the user's personal details with unique username and unique email
```sql
        id bigint generated by default as identity,
        email varchar(40),
        name varchar(40),
        password varchar(100),
        roles varchar(255),
        username varchar(15),
        primary key (id)
```

Movies: Keeps the movies's details and a reference to the creator of the movie with unique title
```sql
        id bigint generated by default as identity,
        created_at timestamp not null,
        updated_at timestamp not null,
        description varchar(300),
        num_of_hates integer,
        num_of_likes integer,
        title varchar(100),
        user_id bigint not null,
        primary key (id)
```

Opinions: Keeps user's opinion per movie with reference to the user and to the movie and with unique combination (movie_id, user_id, opinion_choice)
```sql
        id bigint generated by default as identity,
        created_at timestamp not null,
        updated_at timestamp not null,
        opinion_choice varchar(255),
        movie_id bigint not null,
        user_id bigint not null,
        primary key (id)
```

For simplicity reasons we use in the project two user roles ROLE_USER, ROLE_ADMIN and two movie choices LIKE, HATE. Thus I didnt found the need to create extra tables in the database at the time of the implementation ( Possible feature ).

The application's urls are ROLE PERMISSION BASED secured by Spring Security technology.

## Installation server side with tests

```
cd movierama
mvn clean install
```

## Installation server side without tests

```
cd movierama
mvn clean install -DskipTests
```

## Deployment of server side
```
mvn spring-boot:run
```

## Base url of server side
```
http://localhost:8080
```

## Installation and deployment client side with tests

```
cd movierama-client
npm run ris (this command will run a script which will reinstall as clean the project and start the application )
```

## Base url of client side
```
http://localhost:3000
```


