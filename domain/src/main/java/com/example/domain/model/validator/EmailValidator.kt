package com.example.domain.model.validator

import java.util.regex.Pattern

class EmailValidator : Validator {
    override fun isValid(input: String): Boolean {
        val expression = "^[\\w.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(input)
        return matcher.matches()
    }

    override fun getHintMessage(): String {
        return "Enter Email"
    }

    override fun getErrorMessage(): String {
        return "Email format not correct"
    }
}