package com.buslaev.myfinance.ui.addCategory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Categories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val repository: DaoHelper
) : ViewModel() {

    fun addCategory(category: Categories) {
        addCategoryToDb(category)
    }

    private fun addCategoryToDb(category: Categories) = viewModelScope.launch {
        repository.insertCategory(category)
    }
}