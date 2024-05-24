# SmartLibrary | M295-LB
## Beschreibung
**SmartLibrary** ist ein Bibliotheksverwaltungssystem. Es ermöglicht das Verwalten von Büchern und unterstützt alle Kernprozesse von der Ausleihe bis zur Rückgabe. 
Das System vereinfacht auch die Verwaltung von Nutzerdaten. 
Ziel ist es, die Bibliotheksnutzung effizienter zu gestalten und die administrativen Aufgaben zu reduzieren.

# Visuals
## Datenbankdiagramm
![img.png](img.png)

## Klassendiagramm
![img_1.png](img_1.png)

## Screenshots vom Testing

## Validierungsregeln für Buch-Entität

### 1. `publicationDate`
- **Regel**: Das Veröffentlichungsdatum darf nicht in der Vergangenheit liegen.
- **Implementierung**: Verwendet die Annotation `@FutureOrPresent`, um sicherzustellen, dass das Datum entweder heute oder in der Zukunft liegt.
- **Fehlermeldung**: „Das Veröffentlichungsdatum muss heute oder in der Zukunft liegen.“

### 2. `category`
- **Regel**: Die Kategorie des Buches muss den vordefinierten Kategorien entsprechen.
- **Implementierung**: Verwendet die Annotation `@Pattern` mit einem regulären Ausdruck, der nur auf bestimmte Kategorienamen passt.
- **Fehlermeldung**: „Kategorie muss eine der folgenden sein: Belletristik, Sachbuch, Science Fiction, Biographie, Geschichte, Kinder.“

### 3. `title`
- **Regel**: Der Titel muss zwischen 3 und 100 Zeichen lang sein.
- **Implementierung**: Verwendet die Annotation `@Size`, um Mindest- und Höchstlängenbeschränkungen durchzusetzen.
- **Fehlermeldung**: „Der Titel muss zwischen 3 und 100 Zeichen lang sein.“

### Verwendung im Service Layer


# Berechtigungsmatrix für die Book-Service-Endpoints

Die folgende Matrix beschreibt die Zugriffsberechtigungen für die verschiedenen Endpunkte im `BookController`.

| Endpunkt                    | Beschreibung                                 | ADMIN | GUEST |
|-----------------------------|---------------------------------------------|:-----:|:-----:|
| GET /library/all            | Abrufen aller Bücher                        |   ✔   |   ✔   |
| GET /library/{bookId}       | Abrufen eines Buches nach ID                |   ✔   |   ✔   |
| GET /library/exists/{bookId}| Überprüfen der Existenz eines Buches        |   ✔   |   ✔   |
| GET /library/count          | Zählen aller Bücher                         |   ✔   |   ✔   |
| GET /library/search         | Bücher nach Titel oder Veröffentlichungsdatum suchen | ✔ | ✔   |
| POST /library/create        | Erstellen eines neuen Buchdatensatzes       |   ✔   |   ✘   |
| POST /library/createMultiple| Erstellen mehrerer Buchdatensätze           |   ✔   |   ✘   |
| PUT /library/update/{bookId}| Aktualisieren eines Buchdatensatzes         |   ✔   |   ✘   |
| DELETE /library/{bookId}    | Löschen eines Buchdatensatzes               |   ✔   |   ✘   |
| DELETE /library/all         | Löschen aller Bücher                        |   ✔   |   ✘   |

## Rollendefinitionen
- **ADMIN**: Administratoren haben Vollzugriff auf alle Endpunkte, einschließlich Erstellung, Aktualisierung und Löschung von Buchdatensätzen.
- **GUEST**: Gäste haben ausschließlich Lesezugriff und können keine Daten erstellen, aktualisieren oder löschen.


OpenAPI Dokumentation der Services (Resourcen)

# SQL-Script
```sql
DROP DATABASE IF EXISTS Library;
CREATE DATABASE Library;
USE Library;

-- Zuerst die Tabelle Lending erstellen
CREATE TABLE Lending(
    Lending_ID INT PRIMARY KEY AUTO_INCREMENT, 
    Name_Borrower VARCHAR(64)
);

-- Dann die Tabelle Book erstellen, die einen Fremdschlüssel auf Lending hat
CREATE TABLE Book(
    Book_ID INT PRIMARY KEY AUTO_INCREMENT, 
    Title VARCHAR(64),
    Author VARCHAR(64),
    Publication_Date DATE,
    Category VARCHAR(64),
    Availability BOOLEAN,
    Price DOUBLE(10,2),
    Lending_ID INT,
    FOREIGN KEY (Lending_ID) REFERENCES Lending(Lending_ID)
);
```

# Zusammenfassung


# Autor
## Céline Kölbl
