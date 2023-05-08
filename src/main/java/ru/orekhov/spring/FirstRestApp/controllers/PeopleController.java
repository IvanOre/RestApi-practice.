package ru.orekhov.spring.FirstRestApp.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.orekhov.spring.FirstRestApp.dto.PersonDTO;
import ru.orekhov.spring.FirstRestApp.models.Person;
import ru.orekhov.spring.FirstRestApp.services.PeopleService;
import ru.orekhov.spring.FirstRestApp.util.PersonErrorResponse;
import ru.orekhov.spring.FirstRestApp.util.PersonNotCreatedException;
import ru.orekhov.spring.FirstRestApp.util.PersonNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {

    private final PeopleService peopleService;// внедрили сервис для методов поиска людей спискои и по одному
    private final ModelMapper modelMapper;
@Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper, ModelMapper modelMapper1) {
        this.peopleService = peopleService;
    this.modelMapper = modelMapper1;
}
    @GetMapping()
    public List<PersonDTO> getPeople(){
    return peopleService.findAll().stream().map(this::converToPersonDTO).collect(Collectors.toList());// Jackson конвертирует эти объекты в Json
    }
    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable("id") int id){
        // Статус 200. все ок
    return converToPersonDTO(peopleService.findOne(id));// так же будет автоматом сконвертирован в Json

    }

    @PostMapping    // делаем запрос тела и проверку на валидность нашего персон и бд
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult){
    if (bindingResult.hasErrors()){
    StringBuilder errorMsg = new StringBuilder();

    List<FieldError> errors = bindingResult.getFieldErrors();
    for (FieldError error: errors){// для каждого филдеррор в списке ошибок ерроры
        errorMsg.append(error.getField())// на каком поле была совершена ошибка
                .append(" - ").append(error.getDefaultMessage())// помимо поля где,скажем еще какая ошибка была
                .append(";");// что бы ошибки не склеились в одну разделяем их знаком ;
    }

    throw new PersonNotCreatedException(errorMsg.toString());// выкидываем исключение с сообщением через туСтринг

    }
    peopleService.save(convertToPerson(personDTO));// кладем чела созданного в бд
    return ResponseEntity.ok(HttpStatus.OK);// возвращаем что все хорошо. HTTP с пустым телом и статусом 200
    }


    //специальная аннотация которой мы помечаем. В себя принимает необходимое исключение и
    //возвращает необходимый Json
@ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException e){
// наш персонеррорреспонс внутри с сообщением и датой.
    PersonErrorResponse response = new PersonErrorResponse("Person with this id wasn't found!!!",
            System.currentTimeMillis());
// возвращаем нашу обертку над респонс и сообщением и датой и хттп статусом
    // в HTTP ответе тело ответа(response) и статус в заголовке
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);// статус 404 ошибка

    }
    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException e){
// наш персонеррорреспонс внутри с сообщением кастомным и датой.
        PersonErrorResponse response = new PersonErrorResponse(e.getMessage(),
                System.currentTimeMillis());
// возвращаем нашу обертку над респонс и сообщением и датой и хттп статусом
        // в HTTP ответе тело ответа(response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);//

    }

    private Person convertToPerson(PersonDTO personDTO) {// берем наше DTO

       return modelMapper.map(personDTO, Person.class);// хотим смапить из персонДТО в объект класса Персон
        // берет одинаковые поля из Person & PersonDTO и мапит их

    }
    // еще метод зеркальный выше который. тут мапит модель в ДТО
    private PersonDTO converToPersonDTO(Person person){
    return modelMapper.map(person,PersonDTO.class);

    }

}
