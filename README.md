# The Phoenix Project — LinkedIn Backend

Backend/server component of an academic **LinkedIn-style social network** built in Java.

The project implements users, profiles, posts, comments, likes, follows/connections, education, job positions, feeds, search, direct messaging, file upload/download, and JWT-based authentication.

> **Related repository:** [The-Phoenix-Project-Client](https://github.com/Hessam-Hosseinian/The-Phoenix-Project-Client)

## Main features

- User registration and profile management
- JWT authentication
- Posts and feed generation
- Likes and comments
- Follow / connection flows
- Education and job-position data
- Search and hashtag-related functionality
- Direct messages
- File upload and download
- Persistence through DAO classes
- Unit tests for core models and utilities

## Project structure

```text
src/main/java/com/nessam/server/
├── Server.java
├── config/          # Application configuration
├── controllers/     # Feature controllers
├── dataAccess/      # DAO / persistence layer
├── handlers/
│   ├── httpHandlers/
│   └── modelHandlers/
├── models/          # Domain models
└── utils/           # JWT, JSON, validation, storage, logging
```

Tests are located under:

```text
src/test/java/com/nessam/server/
```

## Tech stack

- Java
- Maven
- Hibernate / JPA
- JWT (`jjwt`)
- JSON serialization libraries
- JUnit / Mockito
- File upload and local storage utilities

## Building

This is a legacy student project and its `pom.xml` contains dependency/version choices from the original development period. A modern JDK may require dependency cleanup before the project builds cleanly.

With Maven installed, start by running:

```bash
mvn test
```

or:

```bash
mvn package
```

The server entry point is:

```text
com.nessam.server.Server
```

For the most reliable reproduction of the original project, importing it as a Maven project in an IDE is recommended.

## Client

The JavaFX desktop client is maintained separately:

[Hessam-Hosseinian/The-Phoenix-Project-Client](https://github.com/Hessam-Hosseinian/The-Phoenix-Project-Client)

## Status

This repository is preserved as an **academic/legacy full-stack project**. It is useful for studying the original architecture and implementation, but it is not presented as a production-ready social network.

## License

No open-source license is currently provided.
