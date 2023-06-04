package com.example.githubservice.controller

import com.example.githubservice.service.UserNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<String> {
        val errorDTO = ExceptionDTO(404, ex.message)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO.toString())
    }

    @ExceptionHandler(NotAcceptableHeaderException::class)
    fun handleNotAcceptableHeaderException(ex: NotAcceptableHeaderException): ResponseEntity<String> {
        val errorDTO = ExceptionDTO(406, ex.message)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO.toString())
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred")
    }
}