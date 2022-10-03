package com.example.submission1androidintermediate.helper

import com.example.domain.model.validator.Validator

sealed class FormType() {
    class Email <T: Validator> (val data: T) : FormType()
    class Password<T: Validator>(val data: T): FormType()
    class Username<T: Validator>(val data: T): FormType()
}