package ru.shukyurov.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.shukyurov.library.models.Person;
import ru.shukyurov.library.services.PeopleService;
import ru.shukyurov.library.util.PersonValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonValidator personValidator;
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PersonValidator personValidator, PeopleService peopleService) {
        this.personValidator = personValidator;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String showPeople(Model model) {
        model.addAttribute("people", peopleService.findAll());

        return "people/showPeople";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {

        return "people/new";
    }

    @PostMapping()
    public String createPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/new";

        peopleService.addPerson(person);

        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findById(id));
        model.addAttribute("listOfBooks", peopleService.getPersonListOfBooksById(id));

        return "people/showPerson";
    }

    @GetMapping("/{id}/edit")
    public String editPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findById(id));

        return "people/editPerson";
    }

    @PatchMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person, @PathVariable("id") int id,
                               BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "people/editPerson";

        peopleService.updatePerson(person, id);

        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        peopleService.deletePerson(id);

        return "redirect:/people";
    }
}
