
# Build Your Setup

A JavaFX desktop application for configuring custom PC builds using object-oriented programming principles and software design patterns.

## Project Overview

Build Your Setup allows users to select PC components, customize their configurations, calculate prices, validate compatibility, and manage saved builds.

The application demonstrates how multiple design patterns can be combined in a practical software system.

## Technologies

- Java 21
- JavaFX
- Maven
- JUnit 5
- Jackson JSON
- IntelliJ IDEA

## Main Features

- Select CPU, GPU, and RAM
- Create custom PC configurations
- Calculate total build prices in KZT
- Add RGB lighting and extended warranty
- Generate component reports
- Export reports to TXT files
- Validate PC configurations
- Save builds to JSON
- Load previously saved builds
- Update existing builds
- Delete saved builds
- Recommend budget and performance configurations
- Dark-themed graphical user interface

## Design Patterns

The project implements eight software design patterns.

### 1. Builder Pattern

**Class:** `PCBuildBuilder`

Constructs PC configurations step by step by adding CPU, GPU, and RAM.

### 2. Factory Method Pattern

Creates component objects through dedicated factory logic, separating object creation from application logic.

### 3. Strategy Pattern

Provides different recommendation strategies, including budget-oriented and performance-oriented PC configurations.

### 4. Observer Pattern

Updates dependent application information when the PC configuration changes.

### 5. Decorator Pattern

**Classes:** `BasicBuild`, `RGBDecorator`, `WarrantyDecorator`

Adds optional features without modifying the original PC build.

- RGB Lighting: +15,000 KZT
- Extended Warranty: +25,000 KZT

### 6. Adapter Pattern

Integrates external component catalog data with the application's internal component model.

### 7. Facade Pattern

Provides a simplified interface for validating PC configurations.

### 8. Visitor Pattern

**Class:** `ReportVisitor`

Generates detailed reports containing component specifications and prices.

## Project Structure

```text
src/
├── main/
│   ├── java/com/buildyoursetup/
│   │   ├── model/
│   │   ├── patterns/
│   │   │   ├── builder/
│   │   │   ├── factory/
│   │   │   ├── strategy/
│   │   │   ├── observer/
│   │   │   ├── decorator/
│   │   │   ├── adapter/
│   │   │   ├── facade/
│   │   │   └── visitor/
│   │   └── ...
│   └── resources/
│       └── styles/
│           └── app.css
└── test/
    └── java/com/buildyoursetup/
```

## How to Run

### Requirements

- JDK 21
- Maven
- JavaFX dependencies configured in `pom.xml`

### Steps

1. Clone the repository.
2. Open the project in IntelliJ IDEA.
3. Load the Maven dependencies.
4. Open the Maven terminal.
5. Run:

```bash
mvn clean javafx:run
```

## Testing

The application includes JUnit 5 unit tests and integration testing.

The integration test verifies the combined behavior of:

- Builder Pattern
- Decorator Pattern
- Visitor Pattern

Run all tests:

```bash
mvn test
```

## Data Storage

Saved configurations are stored in:

```text
data/builds.json
```

The application uses Jackson JSON serialization to store and retrieve PC build information.

## Example Configuration

| Component | Example |
|---|---|
| CPU | AMD Ryzen 5 7600 |
| GPU | NVIDIA RTX 4060 |
| RAM | Corsair Vengeance 32GB |
| RGB Lighting | Enabled |
| Extended Warranty | Enabled |

Example price calculation:

```text
Base Price:       335,000 KZT
RGB Lighting:      15,000 KZT
Extended Warranty: 25,000 KZT
-----------------------------
Final Price:      375,000 KZT
```

*Prices are illustrative test values.*

## Authors

Developed as a team project for the Software Design Patterns course.

## Conclusion

Build Your Setup demonstrates practical applications of eight design patterns in a Java desktop application.

The project combines modular architecture, graphical interaction, persistent storage, validation, automated testing, and report generation.
