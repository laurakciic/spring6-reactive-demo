package com.laurakovacic.spring6reactivedemo.repositories;

import com.laurakovacic.spring6reactivedemo.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository {

    Person luka = Person.builder().id(1).firstName("Luka").lastName("Roncevic").build();
    Person laura = Person.builder().id(2).firstName("Laura").lastName("Kovacic").build();
    Person hana = Person.builder().id(3).firstName("Hana").lastName("Tot").build();
    Person karlo = Person.builder().id(4).firstName("Karlo").lastName("Madz").build();

    @Override
    public Mono<Person> getById(final Integer id) {
        return findAll().filter(person -> person.getId().equals(id)).next();
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(luka, laura, hana, karlo);
    }
}
