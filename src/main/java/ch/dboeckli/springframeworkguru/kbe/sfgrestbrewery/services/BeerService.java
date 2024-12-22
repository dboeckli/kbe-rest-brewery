package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.services;

import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerDto;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerPagedList;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;


public interface BeerService {
    BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);

    BeerDto getById(UUID beerId, Boolean showInventoryOnHand);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);

    BeerDto getByUpc(String upc);

    void deleteBeerById(UUID beerId);
}
