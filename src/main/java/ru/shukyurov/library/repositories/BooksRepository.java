package ru.shukyurov.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shukyurov.library.models.Book;

import java.util.Optional;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    Optional<Book> findByTitleStartingWith(String titleStartingWith);

}
