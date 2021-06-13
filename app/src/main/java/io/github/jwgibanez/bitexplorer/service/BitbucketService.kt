package io.github.jwgibanez.bitexplorer.service

import io.github.jwgibanez.api.models.Repository
import io.github.jwgibanez.api.models.Response
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Url

interface BitbucketService {
    @GET("2.0/repositories")
    fun getRepositories() : Observable<Response<List<Repository>>>
    @GET
    fun getNext(@Url url: String) : Observable<Response<List<Repository>>>
}