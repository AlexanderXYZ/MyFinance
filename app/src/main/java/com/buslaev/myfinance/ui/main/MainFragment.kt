package com.buslaev.myfinance.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.myfinance.MainActivity
import com.buslaev.myfinance.R
import com.buslaev.myfinance.adapters.MainAdapter
import com.buslaev.myfinance.databinding.FragmentMainBinding
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.other.Constants.APP_ACTIVITY
import com.buslaev.myfinance.other.Constants.EXPENSES_BALANCE
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val mBinding get() = _binding!!

    private val mViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var mAdapter: MainAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mObserver: Observer<List<Operation>>

    private lateinit var mBalanceNavigation: BottomNavigationView
    private lateinit var mPeriodsNavigation: BottomNavigationView

    private var balance = INCOME_BALANCE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        APP_ACTIVITY.supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.action_bar_layout)
        }
        initFields()
        setupBalanceNavigation()
        setupPeriodsNavigation()
        setupRecyclerView()
    }

    private fun initFields() {
        APP_ACTIVITY.floatingActionButton.setOnClickListener {
            APP_ACTIVITY.navController.navigate(R.id.action_mainFragment_to_addOperationFragment)
        }
    }

    private fun setupBalanceNavigation() {
        mBalanceNavigation = mBinding.balanceNavigationView
        mBalanceNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.income_menu -> {
                    balance = INCOME_BALANCE
                    mViewModel.incomeOperations.observe(viewLifecycleOwner, mObserver)
                }
                R.id.expenses_menu -> {
                    balance = EXPENSES_BALANCE
                    mViewModel.expensesOperations.observe(viewLifecycleOwner, mObserver)
                }
            }
            return@setOnItemSelectedListener false
        }
    }

    private fun setupPeriodsNavigation() {
        mPeriodsNavigation = mBinding.periodsNavigationView
        mPeriodsNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.day_menu -> {
                    mViewModel.getOperationsByDay(balance)
                }
                R.id.weak_menu -> {
                    mViewModel.getOperationsByWeak(balance)
                }
                R.id.month_menu -> {
                    mViewModel.getOperationsByMonth(balance)
                }
                R.id.year_menu -> {
                    mViewModel.getOperationsByYear(balance)
                }
                R.id.all_time_menu -> {
                    mViewModel.getOperationsByAllTime(balance)
                }
            }
            return@setOnItemSelectedListener false
        }
    }

    private fun setupRecyclerView() {
        mRecyclerView = mBinding.mainRecyclerView
        mRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        mObserver = Observer { list ->
            val allMoney: Double = list.sumOf { it.value }
            mAdapter.setList(list, allMoney)
        }
        mViewModel.incomeOperations.observe(viewLifecycleOwner, mObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}