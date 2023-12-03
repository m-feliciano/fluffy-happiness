package br.com.microservices.costumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/costumers")
public class CostumerController {

    private final CostumerService costumerService;

    public CostumerController(CostumerService costumerService) {
        this.costumerService = costumerService;
    }

    @PostMapping
    public ResponseEntity<CostumerDto> register(@RequestBody @Validated CostumerDto costumerDto) {
        log.info("New costumer registration {}", costumerDto);
        costumerService.save(costumerDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CostumerDto>> findAll() {
        List<Costumer> costumers = costumerService.findAll();
        List<CostumerDto> costumersDto = costumers.stream()
                .map(c -> new CostumerDto(c.getFirstname(), c.getLastname(), c.getEmail()))
                .toList();
        return ResponseEntity.ok().body(costumersDto);
    }
}
