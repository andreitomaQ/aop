package com.example.aop.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PersonController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PersonService personService;
    @Autowired
    private ObjectMapper objectMapper;
    private Person person;
    private Person secondPerson;

    @BeforeEach
    public void init() {
        person = Person.builder().id(1L).name("John").build();
        secondPerson = Person.builder().id(2L).name("Maria").build();
    }

    @Test
    public void getAllPersonsReturnOk() throws Exception {
        List<Person> persons = List.of(person, secondPerson);
        when(personService.getAllPersons()).thenReturn(persons);

        ResultActions result = mockMvc.perform(get("/persons"));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].name").value("John"))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].name").value("Maria"))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(personService, times(1)).getAllPersons();
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void getPersonByIdReturnOk() throws Exception {
        when(personService.getPersonById(anyLong())).thenReturn(person);

        ResultActions result = mockMvc.perform(get("/persons/{id}", 0L));
        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"));

        verify(personService, times(1)).getPersonById(anyLong());
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void createPersonReturnCreated() throws Exception {
        when(personService.createOrUpdatePerson(any())).thenReturn(person);

        ResultActions response = mockMvc.perform(post("/persons")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(person)));

        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.id").value(1));

        verify(personService, times(1)).createOrUpdatePerson(any());
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void updatePersonReturnOk() throws Exception {
        Person personToUpdate = new Person(1L, "Johnny");
        Person updatedPerson = new Person(1L, "Johnny");

        when(personService.createOrUpdatePerson(any(Person.class))).thenReturn(updatedPerson);
//        given(personService.createOrUpdatePerson(ArgumentMatchers.any())).willAnswer(invocation -> invocation.getArguments()[0]);

        ResultActions result = mockMvc.perform(put("/persons/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(personToUpdate)));

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Johnny"));

        verify(personService, times(1)).createOrUpdatePerson(any(Person.class));
        verifyNoMoreInteractions(personService);
    }

    @Test
    public void deletePersonReturnOk() throws Exception {
        doNothing().when(personService).deletePersonById(anyLong());

        mockMvc.perform(delete("/persons/{id}", 1L))
                .andExpect(status().isOk());

        verify(personService, times(1)).deletePersonById(anyLong());
        verifyNoMoreInteractions(personService);
    }

}