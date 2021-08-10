package com.buslaev.myfinance.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Categories
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val repository: DaoHelper
) : ViewModel() {

    private var _categories: MutableLiveData<List<Categories>> = MutableLiveData()
    val categories: LiveData<List<Categories>> get() = _categories
}