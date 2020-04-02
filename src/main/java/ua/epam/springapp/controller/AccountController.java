package ua.epam.springapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.epam.springapp.dto.AccountDto;
import ua.epam.springapp.service.AccountService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public AccountDto get(@PathVariable(value="id") UUID id) {
        return accountService.get(id);
    }

    @GetMapping
    public List<AccountDto> getAll() {
        return accountService.getAll();
    }

    @PostMapping
    public ResponseEntity add(@RequestBody AccountDto accountDto) {
        accountService.add(accountDto);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable(value="id") UUID id, @RequestBody AccountDto accountDto) {
        accountService.update(id, accountDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity remove(@PathVariable(value="id") UUID id) {
        accountService.remove(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}
