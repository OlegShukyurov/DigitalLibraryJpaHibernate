package ru.shukyurov.library.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shukyurov.library.models.Book;
import ru.shukyurov.library.models.Person;
import ru.shukyurov.library.repositories.BooksRepository;
import ru.shukyurov.library.repositories.PeopleRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public Optional<Book> findByTitleStartingWith(String titleStartsWith) {
        if (titleStartsWith != null && !titleStartsWith.isEmpty()) {

            return booksRepository.findByTitleStartingWith(titleStartsWith);
        }
        return Optional.empty();
    }

    public List<Book> findAll(String page_number, String books_per_Page, String to_sort_by_year) {
        List<Book> result = booksRepository.findAll();

        boolean toSortByYear = to_sort_by_year != null ? to_sort_by_year.equals("true") : false;

        if (page_number != null && books_per_Page != null) {

            int pageNumber = Integer.parseInt(page_number);
            int booksPerPage = Integer.parseInt(books_per_Page);

            result = result.stream()
                    .skip(booksPerPage * pageNumber)
                    .limit(booksPerPage)
                    .collect(Collectors.toList());
        }
        if (toSortByYear)
            result = result.stream()
                    .sorted(Comparator.comparingInt(Book::getYearOfWriting))
                    .collect(Collectors.toList());

        return result;
    }

    public Book findById(int id) {
        return booksRepository.findById(id).orElse(null);
    }

    @Transactional
    public void makeOwner(int personId, int bookId) {
        Optional<Book> book = booksRepository.findById(bookId);
        Optional<Person> person = peopleRepository.findById(personId);

        if (book.isPresent() && person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());

            book.get().setOwner(person.get());
            book.get().setTookAt(new Date());
            person.get().getBooks().add(book.get());
        }
    }

    @Transactional
    public void deleteOwner(int bookId) {
        Optional<Book> book = booksRepository.findById(bookId);

        if (book.isPresent()) {
            Hibernate.initialize(book.get().getOwner().getBooks());

            book.get().getOwner().getBooks().remove(book.get());
            book.get().setOwner(null);
            book.get().setTookAt(null);
            book.get().setExpired(false);
        }
    }

    @Transactional
    public void addBook(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void updateBook(int id, Book book) {
        book.setId(id);
        booksRepository.save(book);
    }

    @Transactional
    public void deleteBook(int id) {
        booksRepository.deleteById(id);
    }

}
