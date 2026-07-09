# Feature: Signale erstellen und einer Gleiskante zuordnen

## Übersicht

- **Status:** Abgeschlossen
- **Autor:** Robin Radica
- **Erstellt am:** 2026-07-09
- **Letzte Änderung:** 2026-07-09

## Ziel / Problemstellung

Der Editor soll es erlauben, **Signale** als Fachdatum zu erstellen und sie einer
**Gleiskante** (`TrackEdge`) zuzuordnen. Ein Signal hat keine eigenen Koordinaten,
sondern wird per **Linear Referencing** über eine `EdgeRef` an einer Kante,
einer relativen Position (0.0 … 1.0) und einer **Seite** (Links/Rechts) verankert.
Dadurch bleibt es korrekt, wenn Knoten verschoben werden.

Bisher wurde ein Signal immer auf der rechten Seite abgelegt. Anwender:innen
sollen die Seite beim Platzieren bewusst wählen können.

## Anforderungen

### Funktionale Anforderungen

- [x] Über das Werkzeug **Signal** in der Toolbar lässt sich der Signal-Modus aktivieren.
- [x] Ein Klick nahe einer Gleiskante erzeugt ein Signal und ordnet es der
      **nächstgelegenen** Kante an der angeklickten Position zu.
- [x] Klicks, die keiner Kante nahe genug sind, erzeugen kein Signal.
- [x] Die **Seite** (Links/Rechts) ist über einen Toolbar-Schalter „Signalseite“
      wählbar und gilt für neu erstellte Signale (Standard: Rechts).
- [x] Die Statuszeile zeigt die aktuell gewählte Signalseite an.
- [x] Signale werden im Canvas dargestellt (Markierung + Label seitlich der Kante).

### Nicht-funktionale Anforderungen

- [x] Domänenlogik (`model`, `geometry`) bleibt frei von JavaFX-Importen und ist
      unit-getestet; die Seitenwahl ist reine View-Verdrahtung über bestehende,
      getestete Modelloperationen.
- [x] Architektur-Guardrails werden eingehalten (max. 3 Parameter, max. 500 LOC/Klasse).

## Nutzer / Zielgruppe

Planer:innen, die ein schematisches Gleisnetz aufbauen und Signale als Fachdaten
an Gleiskanten anbringen.

## Ein- und Ausgaben

- **Eingaben:** Werkzeug „Signal“, gewählte Signalseite (Links/Rechts),
  Mausklick auf/nahe einer Gleiskante.
- **Ausgaben:** Neues `Signal` im `TrackNetwork`, referenziert über eine `EdgeRef`
  (Kante + Position + Seite); Darstellung im Canvas; aktualisierte Statuszeile.

## Akzeptanzkriterien

- [x] Mit aktivem Signal-Werkzeug erzeugt ein Klick nahe einer Kante genau ein
      Signal auf dieser Kante.
- [x] Ist „Links“ bzw. „Rechts“ gewählt, sitzt das neue Signal auf der jeweiligen
      Seite der Kante.
- [x] Ein Klick fernab jeder Kante erzeugt kein Signal.
- [x] `mvn verify` läuft grün (Kompilierung, Tests, Static Analysis).

## Offene Fragen / Annahmen

- Umbenennen, Verschieben und Löschen bestehender Signale sowie das Zuordnen zu
  einer anderen Kante sind nicht Teil dieses Features und können als Folge-Feature
  ergänzt werden.

## Referenzen

- Domänenmodell: `Signal`, `EdgeRef`, `Side`, `TrackNetwork.addSignal(...)`
- Geometrie: `Geometry.nearestParam`, `Geometry.distanceToSegment`, `Geometry.offsetPoint`
- View: `NetworkEditorController` (Werkzeug- und Seitenwahl), `NetworkRenderer` (Darstellung)
