package com.buslaev.myfinance.ui.account.createAccount

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val repository: DaoHelper
) : ViewModel() {

    fun addAccount(account: Account) = viewModelScope.launch {
        repository.insertAccount(account)
    }
}