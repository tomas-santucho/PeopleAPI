package com.people.banking.services;

import com.people.banking.entities.Person;
import com.people.banking.entities.Relationship;
import com.people.banking.entities.StatsDTO;
import com.people.banking.exceptions.PersonDoesNotExistException;
import com.people.banking.exceptions.RepeatedPersonException;
import com.people.banking.exceptions.UnderageException;
import com.people.banking.repositories.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        if (Period.between(person.getBornDate(), LocalDate.now()).getYears() < MIN_AGE) throw new UnderageException();
        if (repository.isRepeated(person.getCountry(), person.getDocument().getNumber(), person.getDocument().getType()))
            throw new RepeatedPersonException();
        return repository.save(person);
    }

    public List<Person> getAll() {
        return repository.findAll();
    }

    public Optional<Relationship> getRelationship(long id1, long id2) throws PersonDoesNotExistException {
        var father1 = repository.findById(id1).orElseThrow(PersonDoesNotExistException::new).getFather();
        var father2 = repository.findById(id2).orElseThrow(PersonDoesNotExistException::new).getFather();

        if (Objects.isNull(father1) || Objects.isNull(father2)) return Optional.empty();
        if (Objects.equals(father1, father2)) return Optional.of(Relationship.BROTHER);

        if (Objects.isNull(father1.getFather()) || Objects.isNull(father2.getFather())) return Optional.empty();
        if (Objects.equals(father1.getFather(), father2.getFather())) return Optional.of(Relationship.COUSIN);

        if (Objects.equals(father2, father1.getFather())) return Optional.of(Relationship.UNCLE);

        return Optional.empty();
    }

    public void addFather(long childId, long fatherId) throws PersonDoesNotExistException {
        if (!repository.existsById(childId)) throw new PersonDoesNotExistException(childId);
        if (!repository.existsById(fatherId)) throw new PersonDoesNotExistException(childId);

        repository.updateFather(fatherId, childId);
    }

    public List<StatsDTO> getStats() {
        var countries = repository.getCountries();
        var total = repository.count();
        if (countries.isEmpty()) return new ArrayList<>();

        return countries.stream().map(c ->
                new StatsDTO(c,
                        (repository.countDistinctByCountry(c) * 1.0 / total) * 100
                )).toList();
    }
}