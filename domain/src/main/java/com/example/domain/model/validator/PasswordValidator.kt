package com.example.domain.model.validator

import java.util.regex.Pattern

class PasswordValidator: Validator {
    var message = ""
    override fun isValid(input: String): Boolean {
        val oneDigitPattern = Pattern.compile("^.*[0-9].*$")
        val oneLowerCaseLetter = Pattern.compile("^.*[A-Z].*$")
        val oneSpecialCharacter = Pattern.compile("^" +
        ".*[~!@#\\\$%\\\\^&*()\\\\-_=+\\\\|\\\\[{\\\\]};:'\\\",<.>/?].*" + "$")
        val minSixCharacter = Pattern.compile("^.{6,}$")
        if (!oneDigitPattern.matcher(input).matches()) {
            message = "Should contain one number"
            return false
        }
        if (!minSixCharacter.matcher(input).matches()){
            message = "Six character minimal"
            return false
        }
        if (!oneLowerCaseLetter.matcher(input).matches()){
            message = "Should contain one uppercase letter"
            return false
        }
        if (!oneSpecialCharacter.matcher(input).matches()){
            message = "Should contain one special character"
            return false
        }

        return true
    }

    override fun getHintMessage(): String {
        return "Enter Password"
    }

    override fun getErrorMessage(): String {
        return message
    }
}