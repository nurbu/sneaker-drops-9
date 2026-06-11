package com.pluralsight.sneakerdrops.service;

public class NotFoundException extends RuntimeException {

    public NotFoundException(long id) {
        super("No sneaker found with id " + id);
    }
}
