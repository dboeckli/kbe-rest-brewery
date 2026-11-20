package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.services;

import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.domain.Beer;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.repositories.BeerRepository;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.controller.NotFoundException;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerDto;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerPagedList;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerStyleEnum;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.mappers.BeerMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeerServiceImplTest {

    private final PageRequest pageRequest = PageRequest.of(0, 10);

    @Mock
    private BeerRepository beerRepository;

    @Mock
    private BeerMapper beerMapper;

    @InjectMocks
    private BeerServiceImpl beerService;

    @Test
    void listBeersFindsByNameAndStyle() {
        Beer beer = beer();
        when(beerRepository.findAllByBeerNameAndBeerStyle("Mango Bobs", BeerStyleEnum.ALE, pageRequest))
            .thenReturn(page(beer));
        when(beerMapper.beerToBeerDto(beer)).thenReturn(beerDto());

        BeerPagedList result = beerService.listBeers("Mango Bobs", BeerStyleEnum.ALE, pageRequest, false);

        assertThat(result.getContent()).hasSize(1);
        verify(beerRepository).findAllByBeerNameAndBeerStyle("Mango Bobs", BeerStyleEnum.ALE, pageRequest);
    }

    @Test
    void listBeersFindsByNameOnly() {
        Beer beer = beer();
        when(beerRepository.findAllByBeerName("Mango Bobs", pageRequest)).thenReturn(page(beer));
        when(beerMapper.beerToBeerDto(beer)).thenReturn(beerDto());

        BeerPagedList result = beerService.listBeers("Mango Bobs", null, pageRequest, false);

        assertThat(result.getContent()).hasSize(1);
        verify(beerRepository).findAllByBeerName("Mango Bobs", pageRequest);
    }

    @Test
    void listBeersFindsByStyleOnly() {
        Beer beer = beer();
        when(beerRepository.findAllByBeerStyle(BeerStyleEnum.ALE, pageRequest)).thenReturn(page(beer));
        when(beerMapper.beerToBeerDto(beer)).thenReturn(beerDto());

        BeerPagedList result = beerService.listBeers(null, BeerStyleEnum.ALE, pageRequest, false);

        assertThat(result.getContent()).hasSize(1);
        verify(beerRepository).findAllByBeerStyle(BeerStyleEnum.ALE, pageRequest);
    }

    @Test
    void listBeersFindsAllWithoutFilters() {
        Beer beer = beer();
        when(beerRepository.findAll(pageRequest)).thenReturn(page(beer));
        when(beerMapper.beerToBeerDto(beer)).thenReturn(beerDto());

        BeerPagedList result = beerService.listBeers(null, null, pageRequest, false);

        assertThat(result.getContent()).hasSize(1);
        verify(beerRepository).findAll(pageRequest);
    }

    @Test
    void listBeersIncludesInventoryWhenRequested() {
        Beer beer = beer();
        when(beerRepository.findAll(pageRequest)).thenReturn(page(beer));
        when(beerMapper.beerToBeerDtoWithInventory(beer)).thenReturn(beerDto());

        BeerPagedList result = beerService.listBeers(null, null, pageRequest, true);

        assertThat(result.getContent()).hasSize(1);
        verify(beerMapper).beerToBeerDtoWithInventory(beer);
    }

    @Test
    void getByIdReturnsBeerWithInventory() {
        Beer beer = beer();
        BeerDto beerDto = beerDto();
        when(beerRepository.findById(beer.getId())).thenReturn(Optional.of(beer));
        when(beerMapper.beerToBeerDtoWithInventory(beer)).thenReturn(beerDto);

        BeerDto result = beerService.getById(beer.getId(), true);

        assertThat(result).isSameAs(beerDto);
    }

    @Test
    void getByIdReturnsBeerWithoutInventory() {
        Beer beer = beer();
        BeerDto beerDto = beerDto();
        when(beerRepository.findById(beer.getId())).thenReturn(Optional.of(beer));
        when(beerMapper.beerToBeerDto(beer)).thenReturn(beerDto);

        BeerDto result = beerService.getById(beer.getId(), false);

        assertThat(result).isSameAs(beerDto);
    }

    @Test
    void getByIdThrowsWhenBeerIsMissing() {
        UUID beerId = UUID.randomUUID();
        when(beerRepository.findById(beerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> beerService.getById(beerId, false)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void getByUpcReturnsMappedBeer() {
        Beer beer = beer();
        BeerDto beerDto = beerDto();
        when(beerRepository.findByUpc(beer.getUpc())).thenReturn(beer);
        when(beerMapper.beerToBeerDto(beer)).thenReturn(beerDto);

        BeerDto result = beerService.getByUpc(beer.getUpc());

        assertThat(result).isSameAs(beerDto);
    }

    @Test
    void saveNewBeerPersistsAndReturnsMappedBeer() {
        Beer beer = beer();
        BeerDto beerDto = beerDto();
        when(beerMapper.beerDtoToBeer(beerDto)).thenReturn(beer);
        when(beerRepository.save(beer)).thenReturn(beer);
        when(beerMapper.beerToBeerDto(beer)).thenReturn(beerDto);

        BeerDto result = beerService.saveNewBeer(beerDto);

        assertThat(result).isSameAs(beerDto);
        verify(beerRepository).save(beer);
    }

    @Test
    void updateBeerUpdatesFieldsAndReturnsMappedBeer() {
        Beer beer = beer();
        BeerDto updated = BeerDto.builder()
            .beerName("Updated Name")
            .beerStyle(BeerStyleEnum.PALE_ALE)
            .upc("999999999")
            .price(new BigDecimal("42.00"))
            .build();
        when(beerRepository.findById(beer.getId())).thenReturn(Optional.of(beer));
        when(beerRepository.save(beer)).thenReturn(beer);
        when(beerMapper.beerToBeerDto(beer)).thenReturn(updated);

        BeerDto result = beerService.updateBeer(beer.getId(), updated);

        assertThat(result).isSameAs(updated);
        assertThat(beer.getBeerName()).isEqualTo("Updated Name");
        assertThat(beer.getBeerStyle()).isEqualTo(BeerStyleEnum.PALE_ALE);
        assertThat(beer.getUpc()).isEqualTo("999999999");
        assertThat(beer.getPrice()).isEqualByComparingTo("42.00");
    }

    @Test
    void updateBeerThrowsWhenBeerIsMissing() {
        UUID beerId = UUID.randomUUID();
        when(beerRepository.findById(beerId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> beerService.updateBeer(beerId, beerDto())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteBeerByIdDelegatesToRepository() {
        UUID beerId = UUID.randomUUID();

        beerService.deleteBeerById(beerId);

        verify(beerRepository).deleteById(beerId);
    }

    private Page<Beer> page(Beer beer) {
        return new PageImpl<>(List.of(beer), pageRequest, 1);
    }

    private Beer beer() {
        return Beer.builder()
            .id(UUID.randomUUID())
            .beerName("Mango Bobs")
            .beerStyle(BeerStyleEnum.ALE)
            .upc("0631234200036")
            .build();
    }

    private BeerDto beerDto() {
        return BeerDto.builder().id(UUID.randomUUID()).beerName("Mango Bobs").build();
    }

}
