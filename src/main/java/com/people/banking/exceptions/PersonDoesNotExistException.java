package com.people.banking.exceptions;

public class PersonDoesNotExistException extends Exception {
    public PersonDoesNotExistException() {
    }

    public PersonDoesNotExistException(long id) {
        super("Person with id: "+id+" does not exists");
    }
}
