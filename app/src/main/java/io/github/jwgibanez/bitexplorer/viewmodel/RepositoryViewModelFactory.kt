package io.github.jwgibanez.bitexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RepositoryViewModelFactory(): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = RepositoryViewModel() as T
}