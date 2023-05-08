package ru.orekhov.spring.FirstRestApp.util;

public class PersonNotCreatedException extends RuntimeException{

    public PersonNotCreatedException (String msg){// сообщение об ошибке приняли в аргумент конструктора
        super (msg);// и при помощи супера в RuntimeException
    }
}
