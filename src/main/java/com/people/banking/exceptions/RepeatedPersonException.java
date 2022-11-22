package com.people.banking.exceptions;

public class RepeatedPersonException extends Exception{
    public RepeatedPersonException() {
        super("This person is already in the db");
    }
}
