package io.github.jwgibanez.api.models

import java.io.Serializable

data class Links(
    var watchers: Link?,
    var branches: Link?,
    var tags: Link?,
    var commits: Link?,
    var clone: List<Link?>?,
    var self: Link?,
    var source: Link?,
    var html: Link?,
    var avatar: Link?,
    var hooks: Link?,
    var forks: Link?,
    var downloads: Link?,
    var pullrequests: Link?
) : Serializable