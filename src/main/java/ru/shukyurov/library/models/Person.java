package ru.shukyurov.library.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name = "Person")
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "full_name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 12, max = 100, message = "Name should be between 12 and 100 characters")
    @Pattern(regexp = "^([А-Я][а-я]*\\s[А-Я][а-я]*\\s[А-Я][а-я]*)$",
    message = "Your name should be in this format: 'Surname' 'Name' 'Lastname' for example: 'Иванов Иван Иванович'")
    private String fullName;

    @Column(name = "year_of_birthday")
    @Min(value = 1900, message = "Year should be greater than 1900")
    @Max(value = 2023, message = "Year should be 2023 or smaller")
    private int yearOfBirthday;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    public Person() {
    }

    public Person(String fullName, int yearOfBirthday) {
        this.fullName = fullName;
        this.yearOfBirthday = yearOfBirthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getYearOfBirthday() {
        return yearOfBirthday;
    }

    public void setYearOfBirthday(int yearOfBirthday) {
        this.yearOfBirthday = yearOfBirthday;
    }
}
