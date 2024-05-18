package ru.library.springcourse.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.library.springcourse.models.Person;
import ru.library.springcourse.services.BooksService;
import ru.library.springcourse.services.PeopleService;
import ru.library.springcourse.util.PersonErrorResponse;
import ru.library.springcourse.util.PersonNotFoundException;

@RestController
@RequestMapping("/library")
public class PeopleRestController {

    private final PeopleService peopleService;

    private final BooksService booksService;

    @Autowired
    public PeopleRestController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }


    @GetMapping("/peopleNew/{id}")
    public Person show(@PathVariable("id") int id) {
        System.out.println(peopleService.showWithException(id));
        return peopleService.showWithException(id);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handlerException(PersonNotFoundException e){
        PersonErrorResponse response = new PersonErrorResponse(
                "Человек с таким id не найден",
                System.currentTimeMillis()
        );
        // В HTTP ответе будет тело ответа (response) и статус в заголовке http-ответа
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
