package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.repositories;

import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.domain.Beer;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerStyleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface BeerRepository extends JpaRepository<Beer, UUID> {
    Page<Beer> findAllByBeerName(String beerName, Pageable pageable);

    Page<Beer> findAllByBeerStyle(BeerStyleEnum beerStyle, Pageable pageable);

    Page<Beer> findAllByBeerNameAndBeerStyle(String beerName, BeerStyleEnum beerStyle, Pageable pageable);

    Beer findByUpc(String upc);
}
