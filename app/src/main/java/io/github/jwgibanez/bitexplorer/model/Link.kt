package io.github.jwgibanez.api.models

import java.io.Serializable

data class Link(
    var href: String?,
    var name: String?
) : Serializable