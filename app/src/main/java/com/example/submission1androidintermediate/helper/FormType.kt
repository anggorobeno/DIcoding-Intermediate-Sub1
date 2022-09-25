package com.example.submission1androidintermediate.helper

sealed class FormType(val message: String,val hint: String) {
    class Email(): FormType("Formal email tidak sesuai", "Masukkan Email")
    class Password(): FormType("Min 6 Karakter", "Masukkan Password")
}