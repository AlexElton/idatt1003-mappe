# Fjellturregister

Hike and activity register for mountain trips — Portfolio assignment in IDATx1003 Programmering 1, autumn 2026.

## About the project

A text-based, standalone Java application where users can log mountain hikes,
search through them, share them with other loggers or groups, and view statistics.

## Technologies

| Tool        | Version       |
|-------------|---------------|
| Java (LTS)  | 25            |
| Maven       | 3.9.16        |
| JUnit       | 6.1.3         |
| CheckStyle  | Google rules  |

## Running

From the command line:

```bash
mvn compile
mvn test
mvn exec:java -Dexec.mainClass="edu.ntnu.idi.bidata.fjelltur.App"
```

Or open the project in IntelliJ / VS Code and run `App.main()`.

## Project structure

```
src/main/java/edu/ntnu/idi/bidata/fjelltur/
├── App.java              # Application class with main()
├── model/                # Entity classes (Activity, Person, Group)
├── register/             # Registers with business logic
└── ui/                   # Text-based user interface
src/test/java/            # Unit tests (positive and negative)
docs/uml/                 # Generated UML diagrams
```

## Versioning

The project is versioned with git. Release versions are tagged to match the
version in `pom.xml` (e.g. `v1.0-SNAPSHOT` during development).
