package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MvcExceptionHandlerTest {

    private final MvcExceptionHandler mvcExceptionHandler = new MvcExceptionHandler();

    @Test
    @SuppressWarnings("unchecked")
    void validationErrorHandlerReturnsBadRequestWithViolationMessages() {
        ConstraintViolation<Object> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(violation.getPropertyPath()).thenReturn(path);
        when(path.toString()).thenReturn("beerDto.beerName");
        when(violation.getMessage()).thenReturn("must not be blank");
        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));

        ResponseEntity<List> response = mvcExceptionHandler.validationErrorHandler(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).containsExactly("beerDto.beerName : must not be blank");
    }

    @Test
    void handleBindExceptionReturnsBadRequestWithErrors() {
        BindException exception = new BindException(new Object(), "beerDto");
        exception.reject("invalid", "invalid beer");

        ResponseEntity<List> response = mvcExceptionHandler.handleBindException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).hasSize(1);
    }

}
