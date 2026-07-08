# AGENTS.md

Guidance for AI coding agents (Claude Code and compatible tools) working in this
repository. Read this before making changes.

## Project overview

**Vibe Coding App** is a desktop **frontend application built with JavaFX**.
It is an early-stage project — the goal is to grow it cleanly and keep the
structure understandable as it evolves.

- **Language:** Java 21 (LTS)
- **UI toolkit:** JavaFX 21 (FXML + controllers + CSS)
- **Build tool:** Maven
- **Testing:** JUnit 5 (Jupiter)
- **Module system:** JPMS is used (`src/main/java/module-info.java`)

## Repository layout

```
pom.xml                                  Maven build definition
src/main/java/module-info.java           JPMS module descriptor
src/main/java/com/example/vibeapp/
    App.java                             Application entry point (extends Application)
    MainController.java                  Controller for the main view (view wiring only)
    Greeter.java                         Example of pure, testable domain logic
src/main/resources/com/example/vibeapp/
    main-view.fxml                       Main window layout
    styles.css                           Stylesheet for the UI
src/test/java/com/example/vibeapp/
    GreeterTest.java                     Unit tests for domain logic
```

The base package is `com.example.vibeapp`. Rename it deliberately (and update
`module-info.java`, the FXML `fx:controller`, and `mainClass` in `pom.xml`) if
the project gets a real name — do not leave a half-renamed package.

## Commands

Run all commands from the repository root.

| Task                | Command             |
| ------------------- | ------------------- |
| Build               | `mvn compile`       |
| Run the tests       | `mvn test`          |
| Full verify (build + test) | `mvn verify` |
| Run the app         | `mvn javafx:run`    |
| Clean build outputs | `mvn clean`         |

> Note: `mvn javafx:run` needs a graphical display. In a headless environment
> (like CI or a remote container) it will fail to open a window — that is
> expected. Use `mvn test` / `mvn verify` to validate changes there.

## Conventions (priority: clean & structured)

- **Separate logic from UI.** Keep business/domain logic in plain Java classes
  (see `Greeter`) with **no JavaFX imports**, so it can be unit-tested without
  starting the toolkit. Controllers should only wire that logic to the view.
- **Every non-trivial class or logic change gets a test.** Put tests under
  `src/test/java`, mirroring the main package. Name them `<ClassName>Test`.
- **FXML for layout, CSS for styling.** Avoid hard-coding colors, fonts, or
  sizes in Java; put them in `styles.css`. Build screens in `.fxml` files with a
  matching controller rather than constructing large scene graphs in code.
- **Keep `module-info.java` correct.** New packages that JavaFX must reflect
  over (controllers, FXML-bound classes) need an `opens` entry. New third-party
  modules need a `requires` entry.
- **Code style:** 4-space indentation, one top-level class per file, standard
  Java naming (`PascalCase` types, `camelCase` members, `UPPER_SNAKE_CASE`
  constants). Add a short Javadoc to public classes and non-obvious methods.
- **Small, focused commits** with clear messages describing the *why*.

## Definition of done

Before considering a change complete:

1. `mvn verify` passes (compiles cleanly, all tests green).
2. New or changed logic is covered by a test.
3. `module-info.java`, FXML `fx:controller` references, and `pom.xml`
   `mainClass` are consistent with the code.
4. No unused imports, dead code, or commented-out blocks left behind.

## Good first tasks / how to extend

- **Add a screen:** create `foo-view.fxml` + `FooController` in the resource and
  Java package, load it from `App` or via navigation.
- **Add logic:** create a plain class + its `*Test`, then call it from a
  controller.
- **Add a dependency:** add it to `pom.xml`, then add a `requires` line in
  `module-info.java`.

## Things to avoid

- Do not put business logic or magic values directly inside controllers or
  `App`.
- Do not commit build output (`target/`) or IDE files — they are gitignored.
- Do not add a dependency without also updating `module-info.java`.
- Do not bypass FXML by building complex UIs entirely in Java code.
