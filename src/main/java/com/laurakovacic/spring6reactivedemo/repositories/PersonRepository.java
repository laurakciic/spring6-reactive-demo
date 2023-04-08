package com.laurakovacic.spring6reactivedemo.repositories;

import com.laurakovacic.spring6reactivedemo.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonRepository {

    Mono<Person> getById(Integer id);

    Flux<Person> findAll();
}
