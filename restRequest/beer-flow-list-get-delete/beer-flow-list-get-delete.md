# beer Flow: listBeers → createBeer → getBeerById → getBeerByUpc → deleteBeer

Datei: `restRequest/beer-flow-list-get-delete/beer-flow-list-get-delete.http`

## Ziel

Die Liste abrufen und anschließend ein **eigens angelegtes** Bier per ID und UPC holen und wieder löschen.

## Ablauf

```mermaid
sequenceDiagram
    actor C as HTTP Client (IntelliJ/Bruno)
    participant MVC as kbe-rest-brewery
    participant DB as H2

    C->>MVC: GET /api/v1/beer?pageNumber=0&pageSize=5
    MVC->>DB: SELECT beer
    MVC-->>C: 200 (page.content)

    C->>MVC: POST /api/v1/beer
    MVC->>DB: INSERT beer
    MVC-->>C: 201 (Location: /api/v1/beer/{beerId})

    C->>MVC: GET /api/v1/beer/{beerId}
    MVC-->>C: 200 (beer)

    C->>MVC: GET /api/v1/beerUpc/delete-flow
    MVC-->>C: 200 (beer)

    C->>MVC: DELETE /api/v1/beer/{beerId}
    MVC->>DB: DELETE beer
    MVC-->>C: 204 No Content

    C->>MVC: GET /api/v1/beer/{beerId}
    MVC-->>C: 404 Not Found
```

## Requests (in dieser Reihenfolge)

1. `listBeers` – prüft den Listen-Endpunkt (`200`).
2. `createBeer` – legt ein dediziertes Bier für den Delete-Teil an und speichert `beerId`.
3. `getBeerById` – holt das Bier per `beerId`.
4. `getBeerByUpc` – holt dasselbe Bier über seine UPC.
5. `deleteBeer` – löscht genau dieses selbst angelegte Bier per `beerId`.
6. `getBeerById` – prüft, dass das Bier danach `404` liefert.

## Benötigte Globals

|       Global        |         gesetzt von         |            genutzt von            |
|---------------------|-----------------------------|-----------------------------------|
| `beerId`            | `createBeer`                | `getBeerById`, `deleteBeer`       |
| `traceId`, `spanId` | `../scripts/traceparent.js` | alle Requests (Trace-Korrelation) |

## Asserts

- `listBeers` → `200`.
- `createBeer` → `201` mit ID im `Location`-Header.
- `getBeerById` → `200`, `id` entspricht der `{{beerId}}`.
- `getBeerByUpc` → `200`, `upc` entspricht `delete-flow`.
- `deleteBeer` → `204`.
- `getBeerById` nach dem Delete → `404`.

## Hinweise

- Basis-URL ist `{{application-port}}` (Profil `local` = 8080, `k8s` = 30080).
- `listBeers` liefert eine Page; das erste Element liegt unter `response.body.content[0]`.
- `NotFoundException` ist mit `@ResponseStatus(HttpStatus.NOT_FOUND)` annotiert.

