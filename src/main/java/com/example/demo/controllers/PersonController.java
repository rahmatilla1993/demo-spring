package com.example.demo.controllers;

import com.example.demo.models.Person;
import com.example.demo.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/people")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    public HttpEntity<?> findAll() {
        return ResponseEntity.ok(personService.findAll());
    }

    @GetMapping("/{id}")
    public HttpEntity<?> findById(@PathVariable("id") int id) {
        return ResponseEntity.ok(personService.findById(id));
    }

    @PostMapping
    public HttpEntity<?> save(@RequestBody Person person) {
        personService.save(person);
        return ResponseEntity.ok("Saved");
    }

    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable("id") int id, @RequestBody Person person) {
        personService.update(id, person);
        return ResponseEntity.accepted().body("Edited");
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable("id") int id) {
        personService.delete(id);
        return ResponseEntity.ok("Deleted");
    }
}
