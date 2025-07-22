package br.com.microservices.customer.controller;

import br.com.microservices.customer.service.ICustomerService;
import br.com.microservices.customer.transfer.CustomerDto;
import br.com.microservices.customer.transfer.CustomerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final ICustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDto> register(@RequestBody @Validated CustomerDto customerDto) {
        log.info("New customer registration {}", customerDto);
        customerService.save(customerDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> findAll() {
        List<CustomerDto> list = customerService.findAll().stream()
                .map(CustomerMapper::toDto)
                .toList();
        return ResponseEntity.ok().body(list);
    }
}
