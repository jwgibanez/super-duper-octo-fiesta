package io.github.jwgibanez.bitexplorer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jwgibanez.api.models.Repository
import io.github.jwgibanez.bitexplorer.di.DaggerApiComponent
import io.github.jwgibanez.bitexplorer.service.BitbucketService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryListViewModel : ViewModel() {

    @Inject lateinit var bitbucketService: BitbucketService

    val repositories = MutableLiveData<ArrayList<Repository>>()
    val next = MutableLiveData<String?>()

    private fun fetchRepositories() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                bitbucketService.getRepositories().subscribe {
                    next.postValue(it.next)
                    repositories += it.values
                    //response.postValue(it)
                }
            }
        }
    }

    fun fetchNext() {
        next.value?.let {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    bitbucketService.getNext(it).subscribe {
                        next.postValue(it.next)
                        repositories += it.values
                    }
                }
            }
        }
    }

    operator fun <T> MutableLiveData<ArrayList<T>>.plusAssign(values: List<T>) {
        val value = this.value ?: arrayListOf()
        values.forEach { if (!value.contains(it)) value.add(it) }
        postValue(value)
    }

    init {
        DaggerApiComponent.create().inject(this)
        fetchRepositories()
    }
}