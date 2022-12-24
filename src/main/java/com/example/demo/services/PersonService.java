package com.example.demo.services;

import com.example.demo.models.Person;
import com.example.demo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> findAll() {
        return personRepository.findAll();
    }

    public Person findById(int id) {
        return personRepository
                .findById(id)
                .orElse(null);
    }

    public void update(int id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }

    public void save(Person person) {
        personRepository.save(person);
    }

    public void delete(int id) {
        personRepository
                .findById(id)
                .ifPresent(personRepository::delete);
    }
}
