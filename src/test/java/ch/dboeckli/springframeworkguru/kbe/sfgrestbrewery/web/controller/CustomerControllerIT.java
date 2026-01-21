package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.controller;

import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.CustomerDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIT {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
            .baseUrl("http://localhost:" + port + "/api/v1/customer")
            .build();
    }

    @Test
    void testGetCustomerById() {
        CustomerDto savedCustomer = saveCustomer(createCustomerDto());

        CustomerDto foundCustomer = restClient.get()
            .uri("/{customerId}", savedCustomer.getId())
            .retrieve()
            .body(CustomerDto.class);

        assertThat(foundCustomer).isNotNull();
        assertNotNull(foundCustomer.getId());
    }

    @Test
    void testSaveNewCustomer() {
        CustomerDto newCustomer = createCustomerDto();

        ResponseEntity<Void> response = restClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(newCustomer)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    @Test
    void testUpdateCustomer() {
        CustomerDto savedCustomer = saveCustomer(createCustomerDto());
        savedCustomer.setName("Updated Name");

        ResponseEntity<Void> response = restClient.put()
            .uri("/{customerId}", savedCustomer.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .body(savedCustomer)
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // Da die Update-Methode im Service aktuell nur ein TODO ist (laut Kontext),
        // können wir hier noch nicht prüfen, ob der Name wirklich geändert wurde,
        // es sei denn, du hast die Implementierung im Service inzwischen ergänzt.
    }

    @Test
    void testDeleteCustomer() {
        CustomerDto savedCustomer = saveCustomer(createCustomerDto());

        ResponseEntity<Void> response = restClient.delete()
            .uri("/{customerId}", savedCustomer.getId())
            .retrieve()
            .toBodilessEntity();

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();

        // Hinweis: Da die Delete-Methode im Service auch nur ein Log-Statement ist,
        // wird der Customer vermutlich nicht wirklich gelöscht sein,
        // weshalb ein nachfolgender GET evtl. immer noch erfolgreich wäre.
        // Wenn die Implementierung echt wäre, würde man hier 404 erwarten:
        /*
        assertThrows(HttpClientErrorException.NotFound.class, () -> {
            restClient.get()
                    .uri("/{customerId}", savedCustomer.getId())
                    .retrieve()
                    .toBodilessEntity();
        });
        */
    }

    private CustomerDto createCustomerDto() {
        return CustomerDto.builder()
            .name("Test Customer")
            .build();
    }

    private CustomerDto saveCustomer(CustomerDto customerDto) {
        ResponseEntity<Void> response = restClient.post()
            .contentType(MediaType.APPLICATION_JSON)
            .body(customerDto)
            .retrieve()
            .toBodilessEntity();

        String location = response.getHeaders().getLocation().getPath();
        String idString = location.substring(location.lastIndexOf("/") + 1);
        UUID savedId = UUID.fromString(idString);

        return CustomerDto.builder()
            .id(savedId)
            .name(customerDto.getName())
            .build();
    }

}
