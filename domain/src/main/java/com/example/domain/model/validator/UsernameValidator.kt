package com.example.domain.model.validator

import java.util.regex.Pattern

class UsernameValidator: Validator {
    val minSixCharacter = Pattern.compile("^.{6,}$")

    override fun isValid(input: String): Boolean {
        if (!minSixCharacter.matcher(input).matches()){
            return false
        }
        return true
    }

    override fun getHintMessage(): String {
        return "Enter Username"
    }

    override fun getErrorMessage(): String {
        return "Six character minimal"
    }
}