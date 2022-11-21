package com.people.banking.services;

import com.people.banking.entities.Document;
import com.people.banking.entities.DocumentType;
import com.people.banking.entities.Person;
import com.people.banking.entities.Relationship;
import com.people.banking.exceptions.PersonDoesNotExistException;
import com.people.banking.exceptions.RepeatedPersonException;
import com.people.banking.exceptions.UnderageException;
import com.people.banking.repositories.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonServiceTest {

    @Test
    @DisplayName("WHEN we try to create a person with less than 18 years THEN it should throw and exception")
    public void test1() {
        //GIVEN
        var repository = mock(PersonRepository.class);
        var service = new PersonaService(repository);
        var person = Person.builder().id(1L).country("arg").bornDate(LocalDate.now()).build();
        //WHEN/THEN
        assertThrows(UnderageException.class, () -> service.add(person));
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
        assertEquals(person, newPerson);
    }

    @Test
    @DisplayName("WHEN we try to create a person that has the same id,idType and country THEN it it should throw an exception")
    public void test3() {
        //GIVEN
        var person = Person.builder().id(1L).country("arg").bornDate(LocalDate.now().plusYears(19)).document(new Document(null, DocumentType.DNI, "41123123")).build();
        var repository = mock(PersonRepository.class);
        when(repository.isRepeated(person.getCountry(), person.getDocument().getNumber(), person.getDocument().getType())).thenReturn(true);
        var service = new PersonaService(repository);
        //WHEN/THEN
        assertThrows(RepeatedPersonException.class, () -> service.add(person));
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
        assertThrows(PersonDoesNotExistException.class, () -> service.delete(person.getId()));
    }

    @Test
    @DisplayName("WHEN we call getRelationship with 2 people with the same father THEN it returns BROTHER")
    public void test5() throws PersonDoesNotExistException {
        //GIVEN
        var id1 = 1L;
        var id2 = 2L;
        var repository = mock(PersonRepository.class);
        var father = Person.builder().id(1L).country("arg").bornDate(LocalDate.now().plusYears(19)).build();
        when(repository.findById(id1)).thenReturn(Optional.of(providesPerson(id1, father)));
        when(repository.findById(id2)).thenReturn(Optional.of(providesPerson(id2, father)));
        var service = new PersonaService(repository);
        //WHEN
        var relation = service.getRelationship(id1, id2);
        if (relation.isEmpty()) fail();
        //THEN
        assertEquals(Relationship.BROTHER, relation.get());
    }

    @Test
    @DisplayName("WHEN we call getRelationship with 2 people with the same grandfather THEN it returns COUSIN")
    public void test6() throws PersonDoesNotExistException {
        //GIVEN
        var id1 = 1L;
        var id2 = 2L;
        var repository = mock(PersonRepository.class);
        var father = Person.builder().id(1L).country("arg").bornDate(LocalDate.now().plusYears(19)).build();
        when(repository.findById(id1)).thenReturn(Optional.of(providesPerson(id1, providesPerson(5, father))));
        when(repository.findById(id2)).thenReturn(Optional.of(providesPerson(id2, providesPerson(5, father))));
        var service = new PersonaService(repository);
        //WHEN
        var relation = service.getRelationship(id1, id2);
        if (relation.isEmpty()) fail();
        //THEN
        assertEquals(Relationship.COUSIN, relation.get());
    }

    @Test
    @DisplayName("WHEN we call getRelationship with 2 people and one grandfather is the other one father THEN it returns UNCLE")
    public void test7() throws PersonDoesNotExistException {
        //GIVEN
        var id1 = 1L;
        var id2 = 2L;
        var repository = mock(PersonRepository.class);
        var father = Person.builder().id(1L).country("arg").bornDate(LocalDate.now().plusYears(19)).build();
        when(repository.findById(id1)).thenReturn(Optional.of(providesPerson(id1, father)));
        when(repository.findById(id2)).thenReturn(Optional.of(providesPerson(id2, providesPerson(5, father))));
        var service = new PersonaService(repository);
        //WHEN
        var relation = service.getRelationship(id1, id2);
        if (relation.isEmpty()) fail();
        //THEN
        assertEquals(Relationship.UNCLE, relation.get());
    }

    private Person providesPerson(long id, Person father) {
        return Person.builder().id(id).father(father).build();

    }


}