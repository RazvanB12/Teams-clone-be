package com.example.moodleclone.exceptions;


import com.example.moodleclone.dtos.GenericDTO;
import com.example.moodleclone.dtos.responses.ServerErrorDTO;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler({ConversionFailedException.class,BindException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<GenericDTO> handleRequestAttributesConversionExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ServerErrorDTO("Invalid data in one of the requested parameters!","INVALID_REQUEST")
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<GenericDTO> handleMissingHeaderParameter(MissingRequestHeaderException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ServerErrorDTO("Header parameter '"+e.getHeaderName()+"' is missing!","INVALID_REQUEST")
        );
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<GenericDTO> handleUserValidationException (UserValidationException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"INVALID_INPUT"));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GenericDTO> handleUserAlreadyExistsException(DataIntegrityViolationException e){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ServerErrorDTO("User already exists!","DUPLICATE_ENTRY"));
    }

    @ExceptionHandler(UserAuthException.class)
    public ResponseEntity<GenericDTO> handleAuthExceptions(UserAuthException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ServerErrorDTO(e.getMessage(), "NO_ACCESS"));
    }

    @ExceptionHandler(ConstrainsException.class)
    public ResponseEntity<GenericDTO> handleConstrainsException(ConstrainsException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ServerErrorDTO(e.getMessage(), "INVALID_INPUT"));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<GenericDTO> handleExpiredJwtException(ExpiredJwtException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ServerErrorDTO(e.getMessage(), "TOKEN_EXPIRED"));
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<GenericDTO> handleNotificationNotFoundException(NotificationNotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"NO_NOTIFICATIONS"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<GenericDTO> handleUserNotFoundException(UserNotFoundException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"USER_NOT_FOUND"));
    }

    @ExceptionHandler(ProfilePictureException.class)
    public ResponseEntity<GenericDTO> handleProfilePictureException(ProfilePictureException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"INVALID_PROFILE_PICTURE"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<GenericDTO> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"INVALID_METHOD"));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<GenericDTO> handleIOException(IOException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ServerErrorDTO(e.getMessage(),"ERROR"));
    }

    @ExceptionHandler(AssignmentCompletedException.class)
    public ResponseEntity<GenericDTO> handleAssignmentCompletedException(AssignmentCompletedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"ASSIGNMENT_ALREADY_COMPLETED"));
    }

    @ExceptionHandler(GroupNotFoundException.class)
    public ResponseEntity<GenericDTO> handleGroupNotFoundException(GroupNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"GROUP_NOT_FOUND"));
    }

    @ExceptionHandler(SolutionNotFoundException.class)
    public ResponseEntity<GenericDTO> handleSolutionNotFoundException(SolutionNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"SOLUTION_NOT_FOUND"));
    }

    @ExceptionHandler(AssignmentNotFoundException.class)
    public ResponseEntity<GenericDTO> handleAssignmentNotFoundException(AssignmentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"ASSIGNMENT_NOT_FOUND"));
    }

    @ExceptionHandler(UserNotInGroupException.class)
    public ResponseEntity<GenericDTO> handleUserNotInGroupException(UserNotInGroupException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"USER_NOT_IN_GROUP"));
    }

    @ExceptionHandler(FileSizeException.class)
    public ResponseEntity<GenericDTO> handleFileSizeException(FileSizeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"FILE_SIZE_EXCEEDED"));
    }

    @ExceptionHandler(MessageLengthException.class)
    public ResponseEntity<GenericDTO> handleMessageLengthException(MessageLengthException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"MESSAGE_LENGTH_EXCEEDED"));
    }

    @ExceptionHandler(GroupOwnerException.class)
    public ResponseEntity<GenericDTO> handleGroupOwnerException(GroupOwnerException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"GROUP_OWNER_PROBLEM"));
    }

    @ExceptionHandler(AlreadyInGroupException.class)
    public ResponseEntity<GenericDTO> handleAlreadyInGroupException(AlreadyInGroupException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"USER_IN_GROUP"));
    }

    @ExceptionHandler(GradeCompletedException.class)
    public ResponseEntity<GenericDTO> handleGradeCompletedException(GradeCompletedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"GRADE_COMPLETED"));
    }

    @ExceptionHandler(GradeOutOfBoundsException.class)
    public ResponseEntity<GenericDTO> handleGradeOutOfBoundsException(GradeOutOfBoundsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ServerErrorDTO(e.getMessage(),"GRADE_OUT_OF_BOUNDS"));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<GenericDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ServerErrorDTO("Invalid data in body!","INVALID_DATA"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericDTO> handleUncaughtException(Exception e) {
        String defaultMessage = "Something bad happened :(";
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ServerErrorDTO(defaultMessage,"ERROR"));
    }
}