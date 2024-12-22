package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.mappers;

import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.domain.Beer;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);

    BeerDto beerToBeerDtoWithInventory(Beer beer);

    Beer beerDtoToBeer(BeerDto dto);
}
