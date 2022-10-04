package com.example.core.data.utils

interface Mappable<Response,Model> {
    fun mapToModel(response: Response): Model
}