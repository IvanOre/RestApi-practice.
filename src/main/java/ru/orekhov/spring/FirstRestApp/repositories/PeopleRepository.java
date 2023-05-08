package ru.orekhov.spring.FirstRestApp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.orekhov.spring.FirstRestApp.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person,Integer> {
}
