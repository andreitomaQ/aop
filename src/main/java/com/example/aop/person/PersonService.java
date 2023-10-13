package com.example.aop.person;

import com.example.aop.person.log.Log;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService {
    private final PersonRepository personRepository;

    @Log
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    @Log
    public Person getPersonById(Long id) {
        return personRepository.findById(id).orElseThrow(() -> new RuntimeException("person not found"));
    }

    @Log
    public void deletePersonById(Long id) {
        personRepository.deleteById(id);
    }

    @Log
    public Person createOrUpdatePerson(Person person) {
        return personRepository.save(person);
    }
}
