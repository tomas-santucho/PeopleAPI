package com.people.banking;

import com.people.banking.exceptions.RepeatedPersonException;
import com.people.banking.exceptions.UnderageException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PeopleApp {
    public static void main(String[] args) throws RepeatedPersonException, UnderageException {
        SpringApplication.run(PeopleApp.class, args);
    }
}
