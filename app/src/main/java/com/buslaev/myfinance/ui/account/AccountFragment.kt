package com.buslaev.myfinance.ui.account

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.myfinance.MainActivity
import com.buslaev.myfinance.R
import com.buslaev.myfinance.adapters.AccountsAdapter
import com.buslaev.myfinance.databinding.FragmentAccountBinding
import com.buslaev.myfinance.entities.Account
import com.buslaev.myfinance.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : BaseFragment<FragmentAccountBinding>(FragmentAccountBinding::inflate) {

    private val mViewModel by viewModels<AccountViewModel>()

    @Inject
    lateinit var mAdapter: AccountsAdapter
    private lateinit var mObserver: Observer<List<Account>>
    private lateinit var mRecyclerView: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).apply {
            displayTitle("Accounts")
            hideBottomNav()
        }
        setupAccountsRecyclerView()
        setupObserver()
        setupButtons()
    }

    private fun setupAccountsRecyclerView() {
        mRecyclerView = binding.accountsRecyclerView
        mRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObserver() {
        mObserver = Observer { list ->
            mAdapter.setList(list)
        }
        mViewModel.accounts.observe(viewLifecycleOwner, mObserver)
    }

    private fun setupButtons() {
        binding.addAccountFab.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_accountFragment_to_createAccountFragment)
        }
        binding.historyTransfersButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_accountFragment_to_viewHistoryTransfersFragment)
        }
        binding.createTransferButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_accountFragment_to_createTransferFragment)
        }
    }

}