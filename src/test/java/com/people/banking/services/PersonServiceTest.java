package com.people.banking.services;

import com.people.banking.exceptions.PersonDoesNotExistException;
import com.people.banking.exceptions.RepeatedPersonException;
import com.people.banking.exceptions.UnderageException;
import com.people.banking.entities.Person;
import com.people.banking.repositories.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonServiceTest {

    @Test
    @DisplayName("WHEN we try to create a person with less than 18 years THEN it should throw and exception")
    public void test1(){
        //GIVEN
        var repository = mock(PersonRepository.class);
        var service = new PersonaService(repository);
        var person = Person.builder().id(1L).country("arg").bornDate(LocalDate.now()).build();
        //WHEN/THEN
        assertThrows(UnderageException.class, ()-> service.add(person));
    }

    @Test
    @DisplayName("WHEN we try to create a person with the right params THEN it it should create a new Person")
    public void test2() throws UnderageException, RepeatedPersonException {
        //GIVEN
        var person = Person.builder().id(1L).country("arg").bornDate(LocalDate.now().plusYears(19)).build();
        var repository = mock(PersonRepository.class);
        when(repository.save(person)).thenReturn(person);
        var service = new PersonaService(repository);
        //WHEN
        var newPerson = service.add(person);
        //THEN
        assertEquals(person,newPerson);
    }

    @Test
    @DisplayName("WHEN we try to create a person that has the same id,idType and country THEN it it should throw an exception")
    public void test3() {
        //GIVEN
        var person = Person.builder().id(1L).country("arg").bornDate(LocalDate.now().plusYears(19)).build();
        var repository = mock(PersonRepository.class);
        when(repository.isRepeated(person.getCountry(), person.getDocument().getNumber(), person.getDocument().getType())).thenReturn(0);
        var service = new PersonaService(repository);
        //WHEN/THEN
        assertThrows(RepeatedPersonException.class, ()-> service.add(person));
    }

    @Test
    @DisplayName("WHEN we try to delete a person that does not exists THEN it it should throw an exception")
    public void test4() {
        //GIVEN
        var person = Person.builder().id(1L).country("arg").bornDate(LocalDate.now().plusYears(19)).build();
        var repository = mock(PersonRepository.class);
        when(repository.existsById(person.getId())).thenReturn(false);
        var service = new PersonaService(repository);
        //WHEN/THEN
        assertThrows(PersonDoesNotExistException.class, ()-> service.delete(person.getId()));
    }
}