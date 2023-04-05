package com.varun.reactive;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.varun.reactive.BlockUtil.blockForTermination;

public class ReactiveAsyncRepositoryDemo {
    public static void main(String[] args) throws IOException {
        PersonRepositorySync personRepositorySync = new PersonRepositorySync();
        populatePeopleSync(personRepositorySync);

        // Task(SYNC): Get total salary of person with name as "James"
        Long totalSalary = personRepositorySync.findByName("James")
                .stream()
                .map(Person::income)
                .reduce(Long::sum)
                .orElse(0L);
        System.out.println("Total salary(SYNC): " + totalSalary);

        PersonRepositoryAsync personRepositoryAsync = new PersonRepositoryAsync();
        populatePeopleAsync(personRepositoryAsync);

        // Task(ASYNC): Get total salary of person with name as "James"
        Mono<Long> totalSalaryAsync = personRepositoryAsync.findByName("James")
                .map(Person::income)
                .reduce(0L, Long::sum);
        // Just adding subscribe will result in a blocking operation as the operation will be executed on the same
        // thread. So we specify a scheduler for the subscription.
        totalSalaryAsync
                .subscribeOn(Schedulers.parallel())
                .subscribe(ts -> System.out.println("Total salary(ASYNC): " + ts));

        blockForTermination();
    }

    private static void populatePeopleAsync(PersonRepositoryAsync personRepositoryAsync) {
        List.of(
                new Person("James", 100L),
                new Person("James", 300L),
                new Person("Harry", 50L),
                new Person("Garry", 200L),
                new Person("David", 250L),
                new Person("Andrew", 1000L)
        ).forEach(personRepositoryAsync::savePerson);
    }

    private static void populatePeopleSync(PersonRepositorySync personRepositorySync) {
        List.of(
                new Person("James", 100L),
                new Person("James", 300L),
                new Person("Harry", 50L),
                new Person("Garry", 200L),
                new Person("David", 250L),
                new Person("Andrew", 1000L)
        ).forEach(personRepositorySync::savePerson);
    }
}

class PersonRepositoryAsync {
    private final List<Person> people;

    public PersonRepositoryAsync() {
        this.people = new ArrayList<>();
    }

    public void savePerson(Person person) {
        Mono.fromRunnable(() -> people.add(person))
                .subscribeOn(Schedulers.parallel())
                .then()
                .block();
    }

    public Flux<Person> findByName(String name) {
        return Flux.fromIterable(people)
                .filter(person -> person.name().equals(name));
    }
}

class PersonRepositorySync{
    private final List<Person> people;

    public PersonRepositorySync() {
        this.people = new ArrayList<>();
    }

    public void savePerson(Person person) {
        people.add(person);
    }

    public List<Person> findByName(String name) {
        return people.stream()
                .filter(person -> person.name().equals(name))
                .collect(Collectors.toList());
    }

    public Long getIncome(String name) {
        return people.stream()
                .filter(person -> person.name().equals(name))
                .findFirst()
                .map(Person::income)
                .orElse(-1L);
    }
}

record Person(String name, long income) {}