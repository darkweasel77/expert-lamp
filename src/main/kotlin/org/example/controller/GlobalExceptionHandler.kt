package org.example.controller

import mu.KotlinLogging
import org.example.model.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(value = [NotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFoundException(ex: NotFoundException) {
        logger.info { "not found: ${ex.message}" }
    }

    // anything else
    @ExceptionHandler(value = [Exception::class])
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    fun handleUnknownException(ex: Exception) {
        logger.error { "Something went wrong: ${ex.message}" }
    }

}