package ru.orekhov.spring.FirstRestApp.util;

public class PersonErrorResponse {// в этом классе опишем поля которые будут отправляться при ошибках

    private String message;
    private long timestamp;

    public PersonErrorResponse(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
