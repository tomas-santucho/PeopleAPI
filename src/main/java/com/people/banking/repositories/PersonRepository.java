package com.people.banking.repositories;

import com.people.banking.entities.DocumentType;
import com.people.banking.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query("select (count(p) > 0) from Person p WHERE p.country = ?1 and p.document.number = ?2 and p.document.type = ?3")
    boolean isRepeated(String country, String docNumber, DocumentType type);

    @Modifying
    @Transactional
    @Query(value = "update people p set p.father_id = ?1 where p.id = ?2", nativeQuery = true)
    void updateFather(long fatherId, long childId);

    boolean existsById(long id);

    int countDistinctByCountry(String country);

    @Query(value = "select distinct country from people p", nativeQuery = true)
    List<String> getCountries();

    @Query("UPDATE Person p SET p=?1 where p.id = ?2")
    Person updateById(Person p, long id);
}
