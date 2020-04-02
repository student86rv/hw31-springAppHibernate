package ua.epam.springapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.epam.springapp.dto.SkillDto;
import ua.epam.springapp.service.SkillService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final SkillService skillService;

    @Autowired
    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping("/{id}")
    public SkillDto get(@PathVariable(value="id") UUID id) {
        return skillService.get(id);
    }

    @GetMapping
    public List<SkillDto> getAll() {
        return skillService.getAll();
    }

    @PostMapping
    public ResponseEntity add(@RequestBody SkillDto skillDto) {
        skillService.add(skillDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable(value="id") UUID id, @RequestBody SkillDto skillDto) {
        skillService.update(id, skillDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remove(@PathVariable(value="id") UUID id) {
        skillService.remove(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
