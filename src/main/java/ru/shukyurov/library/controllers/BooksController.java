package ru.shukyurov.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.shukyurov.library.models.Book;
import ru.shukyurov.library.models.Person;
import ru.shukyurov.library.services.BooksService;
import ru.shukyurov.library.services.PeopleService;
import ru.shukyurov.library.util.BookValidator;

import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("/books")
public class BooksController {

    private final BooksService booksService;
    private final PeopleService peopleService;
    private final BookValidator bookValidator;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService, BookValidator bookValidator) {
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.bookValidator = bookValidator;
    }

    @GetMapping("/search")
    public String searchBook (@RequestParam(name = "titleStartsWith", required = false) String titleStartsWith,
                              Model model) {

        Optional<Book> book = booksService.findByTitleStartingWith(titleStartsWith);

        if (book.isPresent()) {
            model.addAttribute("book", book.get());
        }

        return "books/search";
    }

    @GetMapping
    public String showBooks(Model model,
                            @RequestParam(name = "page", required = false) String pageNumber,
                            @RequestParam(name = "books_per_page", required = false) String booksPerPage,
                            @RequestParam(name = "sort_by_year", required = false) String toSortByYear) {

        model.addAttribute("books", booksService.findAll(pageNumber, booksPerPage, toSortByYear));

        return "books/showBooks";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {

        return "books/new";
    }

    @PostMapping()
    public String createBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {

        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors())
            return "books/new";

        booksService.addBook(book);

        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findById(id));
        model.addAttribute("people", peopleService.findAll());

        return "books/showBook";
    }

    @PatchMapping("/selectOwner{id}")
    public String selectOwner(@ModelAttribute Person person, @PathVariable("id") int id) {
        booksService.makeOwner(person.getId(), id);

        return "redirect:/books/{id}";
    }

    @DeleteMapping("/releaseOwner{id}")
    public String releaseOwner(@PathVariable("id") int id) {
        booksService.deleteOwner(id);

        return "redirect:/books/{id}";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", booksService.findById(id));

        return "books/editBook";
    }

    @PatchMapping("/{id}")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult) {

        bookValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors())
            return "books/editBook";

        booksService.updateBook(id, book);

        return "redirect:/books";
    }
    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        booksService.deleteBook(id);

        return "redirect:/books";
    }
}
