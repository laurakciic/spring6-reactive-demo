package com.laurakovacic.spring6reactivedemo.repositories;

import com.laurakovacic.spring6reactivedemo.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void monoByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();     // not preferred way bc of blocking

        System.out.println(person);
    }

    @Test
    void getByIdSubscriber() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.subscribe(person -> {        // == personMono.subscribe(System.out::println);
            System.out.println(person);
        });
    }

    @Test
    void mapOperation() {
        Mono<Person> personMono = personRepository.getById(1);

        personMono.map(person -> {              // == personMono.map(Person::getFirstName).subscribe(System.out::println);
            return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println(firstName);
        });
    }
}