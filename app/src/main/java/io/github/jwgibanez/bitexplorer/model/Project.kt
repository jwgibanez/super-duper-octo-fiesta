package io.github.jwgibanez.api.models

import java.io.Serializable

data class Project(
    var links: Links?,
    var type: String?,
    var name: String?,
    var key: String?,
    var uuid: String?
) : Serializable