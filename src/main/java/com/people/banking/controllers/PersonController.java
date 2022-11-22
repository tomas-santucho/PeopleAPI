package com.people.banking.controllers;

import com.people.banking.entities.Person;
import com.people.banking.exceptions.PersonDoesNotExistException;
import com.people.banking.exceptions.RepeatedPersonException;
import com.people.banking.exceptions.UnderageException;
import com.people.banking.services.PersonaService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PersonController {

    private final PersonaService service;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(service.getAll());
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody Person persona) {
        try {
            return ResponseEntity.ok().body(service.add(persona));
        } catch (UnderageException | RepeatedPersonException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Person persona) {
        try {
            return ResponseEntity.ok().body(service.update(persona));
        } catch (UnderageException | RepeatedPersonException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok().body("Person with id: " + id + " deleted");
        } catch (PersonDoesNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/personas/{fatherId}/padre/{childId}")
    public ResponseEntity<?> addFather(@PathVariable long fatherId, @PathVariable long childId) {
        try {
            service.addFather(childId, fatherId);
            return ResponseEntity.ok().body(fatherId + "is now the father of " + childId);
        } catch (PersonDoesNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/relaciones/{id1}/{id2}")
    public ResponseEntity<?> getRelationships(@PathVariable long id1, @PathVariable long id2) {
        try {
            return ResponseEntity.ok().body(service.getRelationship(id1, id2));
        } catch (PersonDoesNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStats() {
            return ResponseEntity.ok().body(service.getStats());
    }
}
