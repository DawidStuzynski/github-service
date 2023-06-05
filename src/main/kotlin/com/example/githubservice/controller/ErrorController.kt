package com.example.githubservice.controller

import com.example.githubservice.dto.ExceptionDTO
import com.example.githubservice.exception.NotAcceptableHeaderException
import com.example.githubservice.exception.UserNotFoundException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ErrorController(val objectMapper: ObjectMapper) {

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFoundException(ex: UserNotFoundException): ResponseEntity<String> {
        val errorDTO = ExceptionDTO(404, ex.message)
        val jsonErr = objectMapper.writeValueAsString(errorDTO)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonErr)
    }

    @ExceptionHandler(NotAcceptableHeaderException::class)
    fun handleNotAcceptableHeaderException(ex: NotAcceptableHeaderException): ResponseEntity<String> {
        val errorDTO = ExceptionDTO(406, ex.message)
        val jsonErr = objectMapper.writeValueAsString(errorDTO)
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonErr)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred")
    }
}