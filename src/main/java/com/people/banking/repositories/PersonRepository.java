package com.people.banking.repositories;

import com.people.banking.entities.DocumentType;
import com.people.banking.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PersonRepository extends JpaRepository<Person, Long> {
    @Query("select count(e) from Person e WHERE e.country = ?1 and e.document.number = ?2 and e.document.type = ?3")
    int isRepeated(String country, String docNumber, DocumentType type);

    boolean existsById(long id);
}
