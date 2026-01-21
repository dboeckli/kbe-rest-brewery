package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BeerPagedListTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testSerializeDeserializeWithPageRequest() {
        BeerDto beerDto = createBeerDto();

        BeerPagedList pagedList = new BeerPagedList(List.of(beerDto), PageRequest.of(1, 1), 1L);

        String json = objectMapper.writeValueAsString(pagedList);

        BeerPagedList deserializedPagedList = objectMapper.readValue(json, BeerPagedList.class);

        assertAll("BeerPagedList Deserialization with PageRequest",
            () -> assertNotNull(deserializedPagedList),
            () -> assertEquals(1, deserializedPagedList.getContent().size()),
            () -> assertEquals(beerDto.getId(), deserializedPagedList.getContent().getFirst().getId())
        );
    }

    @Test
    void testSerializeDeserializeUnpaged() {
        BeerDto beerDto = createBeerDto();

        // Default constructor uses Unpaged internally if not modified
        BeerPagedList pagedList = new BeerPagedList(List.of(beerDto));

        String json = objectMapper.writeValueAsString(pagedList);

        BeerPagedList deserializedPagedList = objectMapper.readValue(json, BeerPagedList.class);

        assertAll("BeerPagedList Deserialization Unpaged",
            () -> assertNotNull(deserializedPagedList),
            () -> assertEquals(1, deserializedPagedList.getContent().size()),
            () -> assertEquals(beerDto.getId(), deserializedPagedList.getContent().getFirst().getId())
        );
    }

    private BeerDto createBeerDto() {
        return BeerDto.builder()
            .id(UUID.randomUUID())
            .beerName("Beer Name")
            .beerStyle(BeerStyleEnum.ALE)
            .upc("123123123")
            .price(new BigDecimal("12.99"))
            .quantityOnHand(12)
            .createdDate(OffsetDateTime.now())
            .lastUpdatedDate(OffsetDateTime.now())
            .build();
    }

}