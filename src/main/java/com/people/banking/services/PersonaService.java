package com.people.banking.services;

import com.people.banking.exceptions.PersonDoesNotExistException;
import com.people.banking.exceptions.RepeatedPersonException;
import com.people.banking.exceptions.UnderageException;
import com.people.banking.entities.Person;
import com.people.banking.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@AllArgsConstructor
@Service
public class PersonaService {
    public final static int MIN_AGE = 18;
    private final PersonRepository repository;


    public void delete(long id) throws PersonDoesNotExistException {
        if (!repository.existsById(id)) throw new PersonDoesNotExistException();
        repository.deleteById(id);
    }

    public void update(Person person) {

    }

    public Person add(Person person) throws UnderageException, RepeatedPersonException {
        if (Period.between(person.getBornDate(),LocalDate.now()).getYears()<MIN_AGE) throw new UnderageException();
        if (repository.isRepeated(person.getCountry(), person.getDocument().getNumber(), person.getDocument().getType())>0) throw new RepeatedPersonException();
        return repository.save(person);
    }

    public List<Person> getAll() {
        return repository.findAll();
    }
}
