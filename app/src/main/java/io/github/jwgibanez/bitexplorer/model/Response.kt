package io.github.jwgibanez.api.models

data class Response<T>(
    var pagelen: Int,
    var values: T,
    var next: String?
)