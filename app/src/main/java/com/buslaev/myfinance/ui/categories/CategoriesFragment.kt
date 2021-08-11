package com.buslaev.myfinance.ui.categories

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.myfinance.MainActivity
import com.buslaev.myfinance.R
import com.buslaev.myfinance.adapters.CategoriesAdapter
import com.buslaev.myfinance.databinding.FragmentCategoriesBinding
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.other.Constants.EXPENSES_BALANCE
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val mBinding get() = _binding!!

    private val mViewModel by viewModels<CategoriesViewModel>()
    private lateinit var mBalanceNavigation: BottomNavigationView

    @Inject
    lateinit var mAdapter: CategoriesAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mObserver: Observer<List<Categories>>

    private val numberOfColumns = 4


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).displayTitle("Categories")
        setupBalanceNavigation()
        setupAddButton()
        setupRecyclerView()
    }

    private fun setupBalanceNavigation() {
        mBalanceNavigation = mBinding.balanceNavigationView
        underlineMenuItem(mBalanceNavigation.menu.getItem(0))
        mBalanceNavigation.setOnItemSelectedListener {
            removeItemsUnderline()
            underlineMenuItem(it)
            when (it.itemId) {
                R.id.income_menu -> {
                    setupObserver(INCOME_BALANCE)
                }
                R.id.expenses_menu -> {
                    setupObserver(EXPENSES_BALANCE)
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun removeItemsUnderline() {
        val size = mBalanceNavigation.menu.size()
        for (i in 0 until size) {
            val item = mBalanceNavigation.menu.getItem(i)
            item.title = item.title.toString()
        }
    }

    private fun underlineMenuItem(item: MenuItem) {
        val content = SpannableString(item.title)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        item.title = content
    }

    private fun setupAddButton() {
        (activity as MainActivity).floatingActionButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_categoriesFragment_to_addCategoryFragment)
        }
    }

    private fun setupRecyclerView() {
        mRecyclerView = mBinding.categoriesRecyclerView
        mRecyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        }
        setupObserver(INCOME_BALANCE)
    }

    private fun setupObserver(balance: String) {
        mObserver = Observer { list ->
            if (list.isNullOrEmpty()) {
                mAdapter.setList(emptyList())
            } else {
                mAdapter.setList(list)
            }

        }
        if (balance == INCOME_BALANCE) {
            mViewModel.categoriesIncome.observe(viewLifecycleOwner, mObserver)
        } else {
            mViewModel.categoriesExpenses.observe(viewLifecycleOwner, mObserver)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}