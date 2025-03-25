package com.samples.customers.boundary;

import com.samples.customers.domain.Customer;
import com.samples.customers.domain.CustomerState;
import jakarta.validation.ValidationException;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerDtoMapper {

  Customer map(CustomerDto source);

  default CustomerState mapState(String state) {
    return null == state ? null : switch (state) {
      case "active" -> CustomerState.ACTIVE;
      case "locked" -> CustomerState.LOCKED;
      case "disabled" -> CustomerState.DISABLED;
      default -> throw new ValidationException("Invalid state: " + state);
    };
  }

  CustomerDto map(Customer source);

  default String mapState(CustomerState state) {
    return null == state ? null : switch (state) {
      case ACTIVE -> "active";
      case LOCKED -> "locked";
      case DISABLED -> "disabled";
    };
  }

}
