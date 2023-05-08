package ru.orekhov.spring.FirstRestApp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.orekhov.spring.FirstRestApp.models.Person;
import ru.orekhov.spring.FirstRestApp.repositories.PeopleRepository;
import ru.orekhov.spring.FirstRestApp.util.PersonNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }
// обращаемся с помощью репозитория к БД-хибернейт строки из таблицы преобразует в объекты класса Person
    // и возвращается список объектов класса Person и затем мы возвращаем этот список из нашего метода контроллера
    public List<Person> findAll(){// возвращает всех людей в виде списка
        return peopleRepository.findAll();
    }

    public Person findOne(int id){// возвращает одного по id
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElseThrow(PersonNotFoundException::new);
    }
@Transactional
    public void save(Person person){// сохраняем людей в бд
        enrichPerson(person);// добавим в этого чела доп.данные которые назначаются на самом сервере
        peopleRepository.save(person);

    }
    //метод для доп.данных с сервера
    private void enrichPerson(Person person) {

        person.setCreatedAt(LocalDateTime.now());
        person.setUpdatedAt(LocalDateTime.now());
        person.setCreatedWho("ADMIN");
    }
}
