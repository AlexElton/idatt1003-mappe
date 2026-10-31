# Fjellturregister

Tur- og aktivitetsregister for fjellturer — Mappeoppgave i IDATx1003 Programmering 1, høsten 2026.

## Om prosjektet

En tekstbasert, enkeltstående Java-applikasjon der brukere kan loggføre fjellturer,
søke i dem, dele dem med andre loggførere eller grupper, og hente ut statistikk.

## Teknologier

| Verktøy      | Versjon  |
|--------------|----------|
| Java (LTS)   | 25       |
| Maven        | 3.9.16   |
| JUnit        | 6.1.3    |
| CheckStyle   | Google-regler |

## Kjøring

Fra kommandolinjen:

```bash
mvn compile
mvn test
mvn exec:java -Dexec.mainClass="edu.ntnu.iir.bidata.fjelltur.App"
```

Eller åpne prosjektet i IntelliJ / VS Code og kjør `App.main()`.

## Prosjektstruktur

```
src/main/java/edu/ntnu/iir/bidata/fjelltur/
├── App.java              # Applikasjonsklasse med main()
├── model/                # Entitetsklasser (Activity, Person, Group)
├── register/             # Registre med forretningslogikk
└── ui/                   # Tekstbasert brukergrensesnitt
src/test/java/            # Enhetstester (positive og negative)
docs/uml/                 # Genererte UML-diagrammer
```

## Versjonering

Prosjektet versjoneres med git. Release-versjoner merkes med tags som matcher
versjonen i `pom.xml` (f.eks. `v1.0-SNAPSHOT` under utvikling).
