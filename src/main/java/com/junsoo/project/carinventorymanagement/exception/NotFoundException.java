
package com.junsoo.project.carinventorymanagement.exception;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String message, Long id) {
        super(message);
    }
}
