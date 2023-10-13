package com.example.aop.person;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    @Mock
    private PersonRepository personRepository;
    @InjectMocks
    private PersonService personService;
    private Person person;
    private Person secondPerson;

    @BeforeEach
    public void setUp() {
        person = Person.builder().id(1L).name("John").build();
        secondPerson = Person.builder().id(2L).name("Maria").build();
    }

    @Test
    public void createOrUpdatePersonReturnSavedPerson() {
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person savedPerson = personService.createOrUpdatePerson(person);

        assertEquals(1L, savedPerson.getId());
        assertEquals("John", savedPerson.getName());

        verify(personRepository).save(any(Person.class));
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void getAllPersonsReturnListOfAllPersons() {
        List<Person> persons = List.of(person, secondPerson);

        when(personRepository.findAll()).thenReturn(persons);

        List<Person> result = personService.getAllPersons();

        assertThat(result).containsExactlyInAnyOrder(person, secondPerson);
        assertEquals(1L, result.get(0).getId());
        assertEquals("John", result.get(0).getName());
        assertEquals(2L, result.get(1).getId());
        assertEquals("Maria", result.get(1).getName());

        verify(personRepository, times(1)).findAll();
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void getPersonByIdReturnPerson() {
        when(personRepository.findById(anyLong())).thenReturn(Optional.of(person));

        Person result = personService.getPersonById(1L);

        assertThat(result).isNotNull();
        assertEquals(1L, result.getId());
        assertEquals("John", result.getName());

        verify(personRepository, times(1)).findById(anyLong());
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void getPersonByIdThrowsException() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> personService.getPersonById(1L));

        verify(personRepository, times(1)).findById(1L);
        verifyNoMoreInteractions(personRepository);
    }

    @Test
    public void deletePersonByIdTest() {
        doNothing().when(personRepository).deleteById(anyLong());

        personService.deletePersonById(1L);

        verify(personRepository, times(1)).deleteById(1L);
        verifyNoMoreInteractions(personRepository);
    }
}