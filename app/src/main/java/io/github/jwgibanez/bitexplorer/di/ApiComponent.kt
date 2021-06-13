package io.github.jwgibanez.bitexplorer.di

import dagger.Component
import io.github.jwgibanez.bitexplorer.viewmodel.RepositoryListViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface ApiComponent {
    fun inject(apiImpl: ApiImpl)
    fun inject(viewModel: RepositoryListViewModel)
}