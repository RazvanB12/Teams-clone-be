package com.example.moodleclone.exceptions;


public class UserNotInGroupException extends Exception{
    public UserNotInGroupException(String message) {
        super(message);
    }
}