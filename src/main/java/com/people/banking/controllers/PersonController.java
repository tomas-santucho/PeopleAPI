package com.people.banking.controllers;

import com.people.banking.entities.Person;
import com.people.banking.exceptions.RepeatedPersonException;
import com.people.banking.exceptions.UnderageException;
import com.people.banking.exceptions.PersonDoesNotExistException;
import com.people.banking.services.PersonaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@AllArgsConstructor
public class PersonController {

    private final PersonaService service;

    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok().body(service.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Person persona){
        try {
            service.add(persona);
        } catch (UnderageException | RepeatedPersonException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.ok().body(persona);
    }


    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Person persona){
        service.update(persona);
        return ResponseEntity.ok().body(persona);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) throws PersonDoesNotExistException {
        service.delete(id);
        return ResponseEntity.ok().body("Person with id: " + id + " deleted");
    }

}
