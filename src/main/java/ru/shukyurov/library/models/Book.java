package ru.shukyurov.library.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Book")
public class Book {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Title should not be empty")
    private String title;

    @Column(name = "author_name")
    @Size(min = 3, message = "Author name should have 3 or more characters")
    @NotEmpty(message = "Author name should not be empty")
    @Pattern(regexp = "^([А-Я][а-я]*\\s[А-Я][а-я]*)$",
            message = "Author name should be in this format: 'Surname' 'Name' for example: 'Иванов Иван'")
    private String authorName;

    @Column(name = "year_of_writing")
    @Min(value = 500, message = "Year of writing should be greater than 1500")
    @Max(value = 2023, message = "Year of writing should be 2023 or smaller")
    private int yearOfWriting;

    @Column(name = "took_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date tookAt;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    @Transient
    private boolean isExpired;

    public Book() {
    }

    public Book(String title, String authorName, int yearOfWriting) {
        this.title = title;
        this.authorName = authorName;
        this.yearOfWriting = yearOfWriting;
    }

    @Transient
    public boolean isExpired() {
        return isExpired;
    }

    @Transient
    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public Date getTookAt() {
        return tookAt;
    }

    public void setTookAt(Date tookAt) {
        this.tookAt = tookAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getYearOfWriting() {
        return yearOfWriting;
    }

    public void setYearOfWriting(int yearOfWriting) {
        this.yearOfWriting = yearOfWriting;
    }
}
