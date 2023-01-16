package ru.shukyurov.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shukyurov.library.models.Book;
import ru.shukyurov.library.models.Person;
import ru.shukyurov.library.repositories.PeopleRepository;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class PeopleService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Person findById(int id) {
        return peopleRepository.findById(id).orElse(null);
    }

    public Optional<Person> findByFullName(String fullName) {
        return peopleRepository.findByFullName(fullName);
    }

    public List<Book> getPersonListOfBooksById(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            List<Book> books = person.get().getBooks();

            for (Book book : books) {
                Calendar calendar = new GregorianCalendar();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, -10);

                if (calendar.getTime().after(book.getTookAt())) {
                    book.setExpired(true);
                }
            }
            return books;
        }
        return Collections.emptyList();
    }

    @Transactional
    public void addPerson(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(Person person, int id) {
        person.setId(id);
        peopleRepository.save(person);
    }

    @Transactional
    public void deletePerson(int id) {
        Optional<Person> person = peopleRepository.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());
            person.get().getBooks().stream()
                    .forEach(book -> {
                        book.setTookAt(null);
                        book.setExpired(false);
                    });
        }
        peopleRepository.deleteById(id);
    }
}
