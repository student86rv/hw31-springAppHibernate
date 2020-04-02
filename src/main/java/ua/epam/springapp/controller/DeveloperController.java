package ua.epam.springapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.epam.springapp.dto.DeveloperDto;
import ua.epam.springapp.service.DeveloperService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperController {

    private final DeveloperService developerService;

    @Autowired
    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @GetMapping("/{id}")
    public DeveloperDto get(@PathVariable(value="id") UUID id) {
        return developerService.get(id);
    }

    @GetMapping
    public List<DeveloperDto> getAll() {
        return developerService.getAll();
    }

    @PostMapping
    public ResponseEntity add(@RequestBody DeveloperDto developerDto) {
        developerService.add(developerDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable(value="id") UUID id, @RequestBody DeveloperDto developerDto) {
        developerService.update(id, developerDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remove(@PathVariable(value="id") UUID id) {
        developerService.remove(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
