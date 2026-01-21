package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.controller;

import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerDto;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerPagedList;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerStyleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BeerControllerIT {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        // Initialisierung des RestClients mit der dynamischen Port-URL
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port + "/api/v1")
                .build();
    }

    @Test
    void testListBeers() {
        BeerPagedList beerPagedList = restClient.get()
                .uri("/beer")
                .retrieve()
                .body(BeerPagedList.class);

        assertThat(beerPagedList).isNotNull();
        // Da wir nicht wissen, ob die DB leer ist, prüfen wir nur, dass die Liste existiert
        // assertThat(beerPagedList.getContent()).isNotEmpty(); 
    }

    @Test
    void testGetBeerById() {
        // Zuerst ein Bier erstellen, um eine valide ID zu haben
        BeerDto newBeer = createBeerDto();
        BeerDto savedBeer = saveBeer(newBeer);

        BeerDto foundBeer = restClient.get()
                .uri("/beer/{beerId}", savedBeer.getId())
                .retrieve()
                .body(BeerDto.class);

        assertThat(foundBeer).isNotNull();
        assertThat(foundBeer.getId()).isEqualTo(savedBeer.getId());
    }

    @Test
    void testSaveNewBeer() {
        BeerDto newBeer = createBeerDto();

        ResponseEntity<Void> response = restClient.post()
                .uri("/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .body(newBeer)
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    @Test
    void testUpdateBeer() {
        // Vorbereiten
        BeerDto savedBeer = saveBeer(createBeerDto());
        savedBeer.setBeerName("Updated Name");

        // Wir müssen die ID für den Pfad speichern, aber im Body muss sie null sein (wegen @Null Validierung)
        UUID beerId = savedBeer.getId();
        savedBeer.setId(null);

        ResponseEntity<Void> response = restClient.put()
            .uri("/beer/{beerId}", beerId)
            .contentType(MediaType.APPLICATION_JSON)
            .body(savedBeer)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(); // Erwartet 204 No Content

        // Überprüfen, ob das Update geklappt hat
        BeerDto updatedBeer = restClient.get()
            .uri("/beer/{beerId}", beerId)
            .retrieve()
            .body(BeerDto.class);

        assertThat(updatedBeer.getBeerName()).isEqualTo("Updated Name");
    }

    @Test
    void testDeleteBeer() {
        BeerDto savedBeer = saveBeer(createBeerDto());

        ResponseEntity<Void> response = restClient.delete()
                .uri("/beer/{beerId}", savedBeer.getId())
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue(); // Erwartet 204 No Content

        // Sicherstellen, dass es weg ist (Erwartet 404)
        assertThrows(HttpClientErrorException.NotFound.class, () -> restClient.get()
                .uri("/beer/{beerId}", savedBeer.getId())
                .retrieve()
                .toBodilessEntity());
    }

    // --- Hilfsmethoden ---

    private BeerDto createBeerDto() {
        return BeerDto.builder()
                .beerName("Test Beer")
                .beerStyle(BeerStyleEnum.ALE)
                .price(new BigDecimal("10.99"))
                .upc("123123123")
                .build();
    }

    private BeerDto saveBeer(BeerDto beerDto) {
        // Wir nutzen den Location Header, um die ID des neuen Bieres zu extrahieren
        ResponseEntity<Void> response = restClient.post()
                .uri("/beer")
                .contentType(MediaType.APPLICATION_JSON)
                .body(beerDto)
                .retrieve()
                .toBodilessEntity();
        
        String location = response.getHeaders().getLocation().getPath();
        String idString = location.substring(location.lastIndexOf("/") + 1);
        UUID savedId = UUID.fromString(idString);
        
        // Das gespeicherte Objekt abrufen (da saveNewBeer im Controller Void zurückgibt und nur den Header setzt)
        return restClient.get()
                .uri("/beer/{beerId}", savedId)
                .retrieve()
                .body(BeerDto.class);
    }
}
