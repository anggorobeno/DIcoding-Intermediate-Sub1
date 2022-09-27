package com.example.core.data.networkutils

open class SingleEvent< T>(private val content: T) {

    @Suppress("MemberVisibilityCanBePrivate")
    var hasBeendHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeendHandled) {
            null
        } else {
            hasBeendHandled = true
            content
        }
    }

    fun peekContent(): T = content

}