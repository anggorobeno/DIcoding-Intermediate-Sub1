package com.example.core.data.networkutils

interface Mappable<Response,Model> {
    fun mapToModel(response: Response): Model
}