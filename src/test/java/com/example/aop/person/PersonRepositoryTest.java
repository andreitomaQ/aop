package com.example.aop.person;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PersonRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PersonRepository personRepository;

    @Test
    public void findByIdTest() {
        Person person = Person.builder().name("John").build();
        entityManager.persist(person);

        Optional<Person> foundPerson = personRepository.findById(person.getId());

        assertTrue(foundPerson.isPresent());
        assertEquals("John", foundPerson.get().getName());
    }

    @Test
    public void saveTest() {
        Person person = Person.builder().id(10L).name("Alice").build();
        personRepository.save(person);

        Person savedPerson = entityManager.find(Person.class, person.getId());

        assertThat(savedPerson.getId()).isEqualTo(person.getId());
        assertThat(savedPerson.getName()).isEqualTo(person.getName());
    }

    @Test
    public void deleteTest() {
        Person person = Person.builder().name("John").build();
        entityManager.persist(person);

        personRepository.deleteById(1L);

        Person deletedPerson = entityManager.find(Person.class, 1L);

        assertNull(deletedPerson);
    }

    @Test
    public void updateTest() {
        Person person = Person.builder().name("John").build();
        entityManager.persist(person);

        person.setName("Updated Name");

        Person updatedPerson = personRepository.save(person);

        Person retrievedPerson = personRepository.findByName("Updated Name");

        assertNotNull(updatedPerson);
        assertNotNull(retrievedPerson);
        assertEquals("Updated Name", retrievedPerson.getName());
    }
}