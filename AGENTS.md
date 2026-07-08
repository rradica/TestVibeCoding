# AGENTS.md

Guidance for AI coding agents (Claude Code and compatible tools) working in this
repository. Read this before making changes.

## Project overview

**Gleisnetz-Editor** is a desktop **JavaFX** application for drawing a
**topological rail network** and attaching domain data to it:

- **Knoten / nodes** (`TrackNode`) — points in the network (stations, junctions).
- **Kanten / edges** (`TrackEdge`) — track edges connecting two nodes.
- **Fachdaten** such as **signals** (`Signal`) positioned by **linear
  referencing**: a signal belongs to an edge and sits at a relative position
  (0.0 = from-node … 1.0 = to-node) on a chosen side. It has no coordinates of
  its own, so it stays correct when nodes move.

The layout is **topological, not geographic**: node coordinates are schematic
(canvas pixels) and express connectivity, not real-world position.

- **Language:** Java 21 (LTS) · **UI:** JavaFX 21 (FXML + Canvas + CSS)
- **Build:** Maven · **Tests:** JUnit 5 · **Modules:** JPMS

## Architecture (keep these layers separate)

```
com.example.vibeapp            App            JavaFX entry point
com.example.vibeapp.model      TrackNode, TrackEdge, Signal, Side,
                               TrackNetwork   pure domain model (no JavaFX imports)
                               SampleNetworks example topological Switzerland
com.example.vibeapp.geometry   Vec2, Geometry linear-referencing math (no JavaFX)
com.example.vibeapp.view       NetworkEditorController  tool state + mouse input
                               NetworkRenderer          draws the model on a Canvas
```

- **`model` and `geometry` are UI-free** and fully unit-tested — this is the
  core invariant. Keep JavaFX types out of them so the domain logic stays
  testable without the toolkit.
- **`view` is the only package that may import JavaFX.** The controller owns
  transient editing state (active tool, drag/edge-pending selection) and turns
  input into model operations; the renderer only draws.

### Resources & module wiring

```
src/main/resources/com/example/vibeapp/
    network-editor.fxml   toolbar + canvas + status bar (fx:controller = NetworkEditorController)
    styles.css            dark theme
src/main/java/module-info.java   opens ...view to javafx.fxml; exports base package
```

## Architecture guardrails

Hard limits that keep classes and methods small and readable. New and changed
code must respect them — refactor rather than exceed them.

- **Methods: at most 3 parameters.** If a method needs more, introduce a
  parameter object / small record, or split the method. The limit targets
  methods; a simple data holder's constructor may carry the fields it needs, but
  when it would exceed ~4 prefer a builder, a factory, or a `record`.
- **Classes: at most 500 lines of code (LOC).** A class that grows past this is
  doing too much — split it by responsibility.

These limits are meant to be enforced by static analysis in the build (see
[Quality tooling](#quality-tooling)); `mvn verify` should fail on a violation
rather than relying on review to catch it.

## Commands

Run from the repository root.

| Task                | Command             |
| ------------------- | ------------------- |
| Build               | `mvn compile`       |
| Run the tests       | `mvn test`          |
| Full verify         | `mvn verify`        |
| Run the app         | `mvn javafx:run`    |
| Clean               | `mvn clean`         |

> `mvn javafx:run` needs a graphical display. In a headless environment it will
> not open a window — use `mvn test` / `mvn verify` there. (A scene can be
> rendered headless via `Scene.snapshot` under `xvfb-run` with
> `-Dprism.order=sw` if a preview image is needed.)

## Editing model (how the UI works)

The toolbar selects a **tool**; clicks on the canvas act according to it:

- **Auswahl (SELECT):** click a node and drag to move it.
- **Knoten (NODE):** click empty space to add a node.
- **Kante (EDGE):** click a start node, then a target node to connect them.
- **Signal (SIGNAL):** click on/near an edge; the signal is placed at the
  nearest position along that edge (`Geometry.nearestParam`).

`Beispielnetz` reloads the sample Switzerland; `Leeren` empties the network.

## Conventions (priority: clean & structured)

- **Domain logic stays in `model` / `geometry` with no JavaFX imports**, so it
  can be unit-tested. Controllers only wire logic to the view.
- **Every non-trivial class or logic change gets a test** under
  `src/test/java`, mirroring the package. Name them `<ClassName>Test`.
- **Geometry lives in `Geometry`** as pure, side-effect-free static methods —
  add new linear-referencing math there and test it, don't inline it in the view.
- **FXML for layout, CSS for styling.** Don't hard-code colors/sizes in Java;
  put them in `styles.css` and `*.fxml`.
- **Keep `module-info.java` correct.** New FXML-bound packages need an `opens`
  entry; new third-party modules need a `requires` entry.
- **Style:** 4-space indent, one top-level class per file, standard Java naming,
  short Javadoc on public classes and non-obvious methods. Small, focused commits.

## Definition of done

1. `mvn verify` passes (compiles cleanly, all tests green).
2. New or changed logic is covered by a test in `model` or `geometry`.
3. `module-info.java`, FXML `fx:controller`, and `pom.xml` `mainClass` stay
   consistent with the code.
4. No unused imports, dead code, or leftover commented-out blocks.

## How to extend

- **New fachdatum type** (e.g. a balise or speed board): add a model class in
  `model` referencing an edge + relative position (mirror `Signal`), store it in
  `TrackNetwork`, render it in `NetworkRenderer`, add a tool in the controller.
- **New geometry** (e.g. curved edges, distance-in-meters): add pure methods to
  `Geometry` (+ tests) before touching the view.
- **New screen:** add `*-view.fxml` + a controller in `view`, load it from `App`.
- **New dependency:** add it to `pom.xml` and a `requires` line in `module-info.java`.

## Quality tooling

Static analysis and coverage run as part of the Maven build so the guardrails
above and common bug patterns are caught automatically, not just in review.

- **Checkstyle** (`maven-checkstyle-plugin`) — enforces the architecture
  guardrails directly: `ParameterNumber` (max 3, methods) and `FileLength`
  (max 500). Bound to the `verify` phase; a violation fails the build.
- **SpotBugs** (`spotbugs-maven-plugin`) — bytecode static analysis for likely
  bugs (null-dereferences, resource leaks, bad equals/hashCode, …).
- **PMD** (`maven-pmd-plugin`) — complementary source-level rules and duplicate
  detection (CPD).
- **JaCoCo** (`jacoco-maven-plugin`) — test coverage report, with an optional
  minimum-coverage gate on `verify`.

Run everything with `mvn verify`. Keep the rule set small and meaningful — every
enabled rule must earn its place; suppress with a documented reason, don't
weaken the guardrails globally.

## Things to avoid

- Do not import JavaFX into `model` or `geometry`.
- Do not give fachdaten absolute coordinates — reference them to an edge.
- Do not put geometry math or magic values directly in controllers.
- Do not commit build output (`target/`) or IDE files — they are gitignored.
- Do not add a dependency without updating `module-info.java`.
