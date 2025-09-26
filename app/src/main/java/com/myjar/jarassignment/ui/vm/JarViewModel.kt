package com.myjar.jarassignment.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.myjar.jarassignment.createRetrofit
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.data.repository.JarRepository
import com.myjar.jarassignment.data.repository.JarRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.http.Query

class JarViewModel : ViewModel() {

    private val _listStringData = MutableStateFlow<List<ComputerItem>>(emptyList())
    val listStringData: StateFlow<List<ComputerItem>>
        get() = _listStringData

    private val rawData= MutableStateFlow<List<ComputerItem>>(emptyList())

    private val repository: JarRepository = JarRepositoryImpl(createRetrofit())

    fun fetchData() {
        viewModelScope.launch {
            repository.fetchResults().collect { results ->
                rawData.value=results
                _listStringData.value=results
            }
        }
    }
    private val _serchQuery= MutableStateFlow("")
    val searchQuery: StateFlow<String> get()= _serchQuery
    fun updateSearchQuery(query: String){
        _serchQuery.value=query
        _listStringData.value=if(query.isBlank()) rawData.value
        else rawData.value.filter { it.name.contains(query,ignoreCase=true)}
    }
}