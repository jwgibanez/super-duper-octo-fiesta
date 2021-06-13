package io.github.jwgibanez.bitexplorer.di

import io.github.jwgibanez.bitexplorer.service.BitbucketService
import javax.inject.Inject

abstract class ApiImpl {
    @Inject
    lateinit var bitbucketService: BitbucketService
    init {
        DaggerApiComponent.create().inject(this)
    }
}