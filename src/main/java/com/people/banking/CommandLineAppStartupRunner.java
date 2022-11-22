package com.people.banking;

import com.people.banking.entities.Document;
import com.people.banking.entities.DocumentType;
import com.people.banking.entities.Person;
import com.people.banking.exceptions.RepeatedPersonException;
import com.people.banking.exceptions.UnderageException;
import com.people.banking.services.PersonaService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

@Component
@AllArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final PersonaService service;

    @Override
    public void run(String... args) throws RepeatedPersonException, UnderageException {
        //loads some dummy data to the db
        if (!service.getAll().isEmpty()) return;
        var countries = Arrays.asList("ARG", "BR", "UY", "A");

        for (int i = 0; i < 500; i++) {
            var countryIndex = i % (countries.size());
            var date = LocalDate.of(1950, 10, 12);
            int randomNum = ThreadLocalRandom.current().nextInt(1000000, 9999999 + 1);

            var document = new Document(null, DocumentType.DNI, String.valueOf(randomNum));
            var p = Person.builder().
                    country(countries.get(countryIndex)).
                    bornDate(date).
                    document(document).
                    build();

            if (i % 2 == 0) {
                int randomNum1 = ThreadLocalRandom.current().nextInt(1000000, 9999999 + 1);
                var document1 = new Document(null, DocumentType.DNI, String.valueOf(randomNum1));
                p.setFather(Person.builder().
                        country(countries.get(countryIndex)).
                        bornDate(date).
                        document(document1)
                        .build());
            }

            service.add(p);
        }
    }
}