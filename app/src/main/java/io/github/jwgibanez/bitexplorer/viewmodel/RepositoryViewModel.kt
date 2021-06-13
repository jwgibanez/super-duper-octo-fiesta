package io.github.jwgibanez.bitexplorer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jwgibanez.api.models.Repository
import io.github.jwgibanez.bitexplorer.utils.parse
import io.github.jwgibanez.bitexplorer.view.ItemDetailFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RepositoryViewModel : ViewModel() {

    val repositoryName = MutableLiveData<String?>()

    val repositoryOwner = MutableLiveData<String?>()

    val repositoryOwnerAvatar =  MutableLiveData<String?>()

    val repositoryHtml = MutableLiveData<String?>()

    val repositoryValues =
        MutableLiveData<ArrayList<ItemDetailFragment.InfoItemRecyclerViewAdapter.ValuePair>>()

    fun setRepository(repository: Repository?) {
        repository?.apply {
            repositoryName.value = name
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    parse(repositoryValues, repository)
                }
            }
        }
    }

}