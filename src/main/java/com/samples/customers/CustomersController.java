package com.samples.customers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;
import java.util.stream.Stream;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomersController {

  private final CustomersService customersService;

  @GetMapping(
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  Stream<Customer> getAllCustomers() {
    return customersService
      .findAll();
  }

  @GetMapping(
    path = "/{id}",
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  Customer getCustomerById(
    @PathVariable("id")
    UUID uuid
  ) {
    return customersService
      .findById(uuid)
      .orElseThrow(NotFoundException::new);
  }


  @PostMapping(
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
  )
  ResponseEntity<Customer> createCustomer(
    @Valid
    @RequestBody
    Customer customer
  ) {
    customersService.create(customer);
    // Location-Header
    var location = ServletUriComponentsBuilder
      .fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(customer.getUuid())
      .toUri();
    // Response
    return ResponseEntity
      .created(location)
      .body(customer);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteCustomer(
    @PathVariable("id")
    UUID uuid
  ) {
    if (!customersService.delete(uuid)) {
      throw new NotFoundException();
    }
  }

}
