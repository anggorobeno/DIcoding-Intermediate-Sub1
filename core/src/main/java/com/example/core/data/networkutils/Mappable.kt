package com.example.core.data.networkutils

interface Mappable<T,H> {
    fun mapToModel(response: H): T
}