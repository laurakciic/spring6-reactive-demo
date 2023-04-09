package com.laurakovacic.spring6reactivedemo.repositories;

import com.laurakovacic.spring6reactivedemo.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepository personRepository = new PersonRepositoryImpl();

    @Test
    void monoGetByIdBlock() {
        Mono<Person> personMono = personRepository.getById(1);

        Person person = personMono.block();     // not preferred way bc of blocking

        System.out.println(person);
    }

    @Test
    void monoGetByIdSubscriber() {
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

    @Test
    void fluxBlockFirst() {
        Flux<Person> personFlux = personRepository.findAll();

        Person person = personFlux.blockFirst();    // blocks and waits for the first element to come across

        System.out.println(person);
    }

    @Test
    void fluxSubscriber() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.subscribe(person -> {            // == personFlux.subscribe(System.out::println);
            System.out.println(person);
        });
    }

    @Test
    void fluxMap() {
        Flux<Person> personFlux = personRepository.findAll();

        personFlux.map(Person::getFirstName).subscribe(firstName -> System.out.println(firstName)); // or (System.out::println)
    }

    @Test
    void fluxToList() {
        Flux<Person> personFlux = personRepository.findAll();

        Mono<List<Person>> listMono = personFlux.collectList(); // converting flux to a mono, returns mono with list element

        listMono.subscribe(list -> {
            list.forEach(person -> System.out.println(person.getFirstName()));
        });
    }

    @Test
    void filterOnName() {
        personRepository.findAll()
                .filter(person -> person.getFirstName().equals("Laura"))
                .subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void getById() {
        Mono<Person> lauraMono = personRepository.findAll().filter(person -> person.getFirstName().equals("Laura"))
                .next();    // filtering flux, next returns a mono of a type

        lauraMono.subscribe(person -> System.out.println(person.getFirstName()));
    }
}