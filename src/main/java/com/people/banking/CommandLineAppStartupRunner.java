package com.people.banking;

import com.people.banking.entities.Person;
import com.people.banking.services.PersonaService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@AllArgsConstructor
public class CommandLineAppStartupRunner implements CommandLineRunner {
    private final PersonaService service;

    @Override
    public void run(String... args) {
        if (!service.getAll().isEmpty()) return;
        var countries = Arrays.asList("ARG", "BR", "UY", "A");

        for (int i = 0; i < 500; i++) {
            var countryIndex = i % (countries.size());
            var p = Person.builder().country(countries.get(countryIndex)).build();

            if (i % 2 == 0) p.setFather(Person.builder().country(countries.get(countryIndex)).build());

            System.out.println("index:" + countryIndex);

            //service.add(p);
        }
    }
}