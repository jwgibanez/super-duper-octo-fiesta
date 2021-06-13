package io.github.jwgibanez.api.models

import java.io.Serializable

data class Branch(
    var type: String?,
    var name: String?
) : Serializable