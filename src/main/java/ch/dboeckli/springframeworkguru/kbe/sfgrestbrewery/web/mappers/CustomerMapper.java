package ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.mappers;

import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.domain.Customer;
import ch.dboeckli.springframeworkguru.kbe.sfgrestbrewery.web.dto.CustomerDto;
import org.mapstruct.Mapper;

/**
 * Created by jt on 2019-05-25.
 */
@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDto dto);

    CustomerDto customerToCustomerDto(Customer customer);
}
