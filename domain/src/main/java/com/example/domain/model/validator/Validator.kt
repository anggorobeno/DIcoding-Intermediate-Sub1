package com.example.domain.model.validator

interface Validator {
    fun isValid(input: String): Boolean
    fun getHintMessage(): String
    fun getErrorMessage(): String
}