package br.com.microservices.costumer;

import br.com.microservices.costumer.transfer.CostumerDto;
import br.com.microservices.costumer.transfer.CostumerMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/costumers")
public class CostumerController {

    private final CostumerService costumerService;

    @PostMapping
    public ResponseEntity<CostumerDto> register(@RequestBody @Validated CostumerDto costumerDto) {
        log.info("New costumer registration {}", costumerDto);
        costumerService.save(costumerDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CostumerDto>> findAll() {
        List<CostumerDto> dtos = costumerService.findAll().stream()
                .map(CostumerMapper::toDto)
                .toList();
        return ResponseEntity.ok().body(dtos);
    }
}
