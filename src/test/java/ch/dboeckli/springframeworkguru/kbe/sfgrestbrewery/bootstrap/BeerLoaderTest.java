package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.bootstrap;

import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.domain.Beer;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.repositories.BeerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeerLoaderTest {

    @Mock
    private BeerRepository beerRepository;

    @Test
    void doesNotLoadBeersWhenRepositoryAlreadyContainsData() {
        when(beerRepository.count()).thenReturn(1L);

        new BeerLoader(beerRepository).run();

        verify(beerRepository, never()).save(any(Beer.class));
    }

    @Test
    void loadsBeersIntoEmptyRepository() {
        when(beerRepository.count()).thenReturn(0L, 0L, 30L);

        new BeerLoader(beerRepository).run();

        verify(beerRepository, times(30)).save(any(Beer.class));
    }

}
