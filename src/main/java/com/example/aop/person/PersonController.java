package com.example.aop.person;

import com.example.aop.person.log.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;

    @Log
    @GetMapping
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }

    @Log
    @GetMapping("/{id}")
    public Person getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id);
    }

    @Log
    @ResponseStatus(CREATED)
    @PostMapping
    public Person createPerson(@RequestBody Person person) {
        return personService.createOrUpdatePerson(person);
    }

    @Log
    @PutMapping("/{id}")
    public Person updatePerson(@PathVariable Long id, @RequestBody Person person) {
        person.setId(id);
        return personService.createOrUpdatePerson(person);
    }

    @Log
    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id) {
        personService.deletePersonById(id);
    }
}
