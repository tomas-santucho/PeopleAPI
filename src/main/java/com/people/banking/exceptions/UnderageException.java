package com.people.banking.exceptions;

public class UnderageException extends Exception{
    public UnderageException() {
        super("Person does not meet the age requirement");
    }
}
