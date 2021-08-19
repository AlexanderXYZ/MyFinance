package com.buslaev.myfinance.ui.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.buslaev.myfinance.db.room.DaoHelper
import com.buslaev.myfinance.entities.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repository: DaoHelper
) : ViewModel() {

    val accounts: LiveData<List<Account>> = repository.getAccounts()
}