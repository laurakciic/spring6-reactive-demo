package com.laurakovacic.spring6reactivedemo.repositories;

import com.laurakovacic.spring6reactivedemo.domain.Person;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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
    void getByIdHardCoded() {
        Mono<Person> lauraMono = personRepository.findAll().filter(person -> person.getFirstName().equals("Laura"))
                .next();    // filtering flux, next returns a mono of a type

        lauraMono.subscribe(person -> System.out.println(person.getFirstName()));
    }

    @Test
    void getByIdFound() {
        final Integer id = 2;
        Mono<Person> personMono = personRepository.getById(id);

        assertTrue(personMono.hasElement().block());
    }

    @Test
    void getByIdFoundStepVerifier() {
        final Integer id = 2;
        Mono<Person> personMono = personRepository.getById(id);

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.getFirstName());
        });
    }

    @Test
    void getByIdNotFound() {
        final Integer id = 12;
        Mono<Person> personMono = personRepository.getById(id);

        assertFalse(personMono.hasElement().block());
    }

    @Test
    void getByIdNotFoundStepVerifier() {
        final Integer id = 12;
        Mono<Person> personMono = personRepository.getById(id);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(person.getFirstName());
        });
    }

    @Test
    void findPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final Integer id = 8;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single() // single throws an exception, next does not
                .doOnError(throwable -> {
                    System.out.println("Error occurred in flux.");
                    System.out.println(throwable.toString());
                });

        personMono.subscribe(person -> {
            System.out.println(person);
        }, throwable -> {
            System.out.println("Error occurred in the mono.");
            System.out.println(throwable.toString());
        });
    }
}