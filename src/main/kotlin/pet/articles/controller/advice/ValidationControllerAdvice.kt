package pet.articles.controller.advice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import pet.articles.tool.exception.DuplicateUserException

object ValidationError {

    const val ERROR = "validationError"

    object ResponseContentTypes {
        const val FIELD = "field"
        const val LIST_OF_FIELD_ERRORS = "listOfFiledErrors"
    }

    object ErrorTypes {
        const val DUPLICATE_FIELD = "duplicateField"
        const val INVALID_FIELD = "invalidField"
    }
}

@ControllerAdvice
class ValidationControllerAdvice {

    @ExceptionHandler(DuplicateUserException::class)
    fun handleDuplicateUserException(e: DuplicateUserException): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(mapOf(
                    ValidationError.ERROR to ValidationError.ErrorTypes.DUPLICATE_FIELD,
                    ValidationError.ResponseContentTypes.FIELD to e.fieldName
            ))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException): ResponseEntity<Map<String, String>> {
        val errors: List<String> = extractListOfErrors(e)
        return ResponseEntity
            .unprocessableEntity()
            .body(mapOf(
                    ValidationError.ERROR to ValidationError.ErrorTypes.INVALID_FIELD,
                    ValidationError.ResponseContentTypes.LIST_OF_FIELD_ERRORS to errors.toString()
            ))
    }

    private fun extractListOfErrors(e: MethodArgumentNotValidException): List<String> =
        e.bindingResult.fieldErrors
            .map { error -> "${error.field}: ${error.defaultMessage}" }
}