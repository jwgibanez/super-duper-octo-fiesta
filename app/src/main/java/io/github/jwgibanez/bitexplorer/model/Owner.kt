package io.github.jwgibanez.api.models

import java.io.Serializable

data class Owner(
    var display_name: String?,
    var uuid: String?,
    var links : Links?,
    var type: String?,
    var nickname: String?,
    var account_id: String?
) : Serializable