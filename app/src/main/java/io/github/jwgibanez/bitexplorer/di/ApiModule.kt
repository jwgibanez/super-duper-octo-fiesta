package io.github.jwgibanez.bitexplorer.di

import dagger.Module
import dagger.Provides
import io.github.jwgibanez.bitexplorer.service.BitbucketService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class ApiModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        val httpClient = OkHttpClient.Builder()
        httpClient.apply {
            addInterceptor(
                HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BODY
                })
        }
        return Retrofit.Builder()
            .baseUrl("https://api.bitbucket.org/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }
    @Singleton
    @Provides
    fun provideRepositoryService(retrofit: Retrofit): BitbucketService {
        return retrofit.create(BitbucketService::class.java)
    }
}