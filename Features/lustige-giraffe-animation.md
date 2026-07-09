# Feature: Lustige Giraffe zeichnet neue Elemente

## Übersicht

- **Status:** Abgeschlossen
- **Autor:** Claude Code
- **Erstellt am:** 2026-07-09
- **Letzte Änderung:** 2026-07-09

## Ziel / Problemstellung

Das Anlegen neuer Elemente (Knoten, Kanten, Signale) soll spielerischer und
sichtbarer werden. Statt dass ein Element schlagartig erscheint, taucht eine
kleine, lustige Cartoon-Giraffe auf und „zeichnet" das neue Element vor den
Augen der Nutzerin animiert in die Fläche. Das gibt visuelles Feedback darüber,
was gerade entstanden ist, und macht den Editor sympathischer.

## Anforderungen

### Funktionale Anforderungen

- [x] Beim Hinzufügen eines **Knotens** erscheint die Giraffe und zeichnet den
      Knoten (wachsender Kreis) an der Klickposition.
- [x] Beim Hinzufügen einer **Kante** wird die Linie vom Start- zum Zielknoten
      animiert gezogen.
- [x] Beim Hinzufügen eines **Signals** wird der Signalarm von der Gleiskante
      bis zum Marker animiert gezeichnet.
- [x] Die Giraffe steht neben der „Stiftspitze" und wackelt leicht mit dem Kopf,
      eine gestrichelte Linie verbindet ihre Schnauze mit der wachsenden Spitze.
- [x] Das Element wird erst nach Abschluss der Animation ins Modell übernommen,
      sodass es tatsächlich gezeichnet erscheint statt sofort dazustehen.
- [x] Wird während einer laufenden Animation eine weitere Aktion ausgelöst
      (schnelles Hinzufügen, Werkzeugwechsel, „Leeren", „Beispielnetz"), wird
      die aktuelle Animation sauber abgeschlossen; kein Element geht verloren.

### Nicht-funktionale Anforderungen

- [x] Die Animation lebt vollständig im `view`-Paket; `model` und `geometry`
      bleiben JavaFX-frei und testbar.
- [x] Die reine Zeit-/Interpolationsmathematik (`Easing.smoothStep`,
      `Vec2.lerp`) liegt im `geometry`-Paket und ist per Unit-Test abgedeckt.
- [x] Architektur-Guardrails eingehalten (max. 3 Parameter, max. 500 LOC/Klasse);
      `mvn verify` (Checkstyle, PMD, SpotBugs, JaCoCo) läuft grün.

## Nutzer / Zielgruppe

Alle, die den Gleisnetz-Editor bedienen und neue Netzelemente anlegen.

## Ein- und Ausgaben

- **Eingaben:** Mausklicks der Werkzeuge Knoten / Kante / Signal.
- **Ausgaben:** Kurze Zeichen-Animation (~1,1 s) auf dem Canvas; danach ist das
  neue Element Teil des Netzes.

## Akzeptanzkriterien

- [x] Nach dem Anlegen jedes Elementtyps ist für kurze Zeit eine Giraffe zu
      sehen, die das Element zeichnet, danach ist das Element vorhanden.
- [x] Die Domänenlogik bleibt ohne JavaFX-Abhängigkeit; alle Tests grün.
- [x] `mvn verify` ist erfolgreich.

## Umsetzung (Kurzüberblick)

- `geometry/Easing.java` – `smoothStep` für sanftes Ein-/Ausblenden der Bewegung.
- `geometry/Vec2.java` – `lerp` für das Wachsen entlang einer Strecke.
- `view/DrawTarget.java` – beschreibt Geometrie des neuen Elements (Kind + Start/Ende).
- `view/Giraffe.java` – zeichnet die Cartoon-Giraffe (Körper, Flecken, Hals, Kopf).
- `view/GiraffeAnimator.java` – `AnimationTimer`, der pro Frame das Netz neu
  zeichnet, das wachsende Element und die Giraffe darüber legt und das Element am
  Ende via Callback ins Modell übernimmt.
- `view/NetworkEditorController.java` – löst die Animation beim Anlegen von
  Knoten/Kante/Signal aus.

## Offene Fragen / Annahmen

- Annahme: Eine feste Animationsdauer (~1,1 s) ist angenehm; bei Bedarf leicht
  über `GiraffeAnimator.DURATION_NANOS` anpassbar.
- Die Animation ist rein kosmetisch und wird von JaCoCo (wie `view`/`App`) nicht
  auf Coverage geprüft; die zugrunde liegende Mathematik ist getestet.

## Referenzen

- [`AGENTS.md`](../AGENTS.md) – Architektur, Guardrails, Definition of Done.
