package io.github.jwgibanez.api.models

import java.io.Serializable

data class Repository(
    var scm: String?,
    var website: String?,
    var has_wiki: Boolean?,
    var uuid: String?,
    var links: Links?,
    var fork_policy: String?,
    var full_name: String?,
    var name: String?,
    var project: Project?,
    var language: String?,
    var created_on: String?,
    var mainbranch: Branch?,
    var workspace: Workspace?,
    var has_issues: Boolean?,
    var owner: Owner?,
    var updated_on: String?,
    var size: Long?,
    var type: String?,
    var slug: String?,
    var is_private: Boolean?,
    var description: String?
) : Serializable {
    override fun equals(other: Any?): Boolean {
        return if (other is Repository) this.uuid?.equals(other.uuid) == true else false
    }

    override fun hashCode(): Int {
        return uuid?.hashCode() ?: 0
    }
}