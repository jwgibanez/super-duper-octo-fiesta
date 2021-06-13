package io.github.jwgibanez.api.models

import java.io.Serializable

data class Workspace(
    var slug: String?,
    var type: String?,
    var name: String?,
    var links: Links?,
    var uuid: String?
) : Serializable