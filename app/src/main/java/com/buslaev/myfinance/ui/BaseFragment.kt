package com.buslaev.myfinance.ui

import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.buslaev.myfinance.R
import com.buslaev.myfinance.other.Constants
import com.google.android.material.bottomnavigation.BottomNavigationView

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    //protected abstract val mViewModel: VM

    private var _binding: VB? = null
    val binding get() = _binding!!

    //var balance: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun removeItemsUnderline(menu: Menu) {
        val size = menu.size()
        for (i in 0 until size) {
            val item = menu.getItem(i)
            item.title = item.title.toString()
        }
    }

    fun underlineMenuItem(item: MenuItem) {
        val content = SpannableString(item.title)
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        item.title = content
    }

//    fun setupBalanceNavigation(balanceNavigation: BottomNavigationView) {
//        underlineMenuItem(balanceNavigation.menu.getItem(0))
//        balanceNavigation.setOnItemSelectedListener {
//            removeItemsUnderline(balanceNavigation.menu)
//            underlineMenuItem(it)
//            when (it.itemId) {
//                R.id.income_menu -> {
//                    balance = Constants.INCOME_BALANCE
//                    mViewModel.incomeOperations.observe(viewLifecycleOwner, mObserver)
//                }
//                R.id.expenses_menu -> {
//                    balance = Constants.EXPENSES_BALANCE
//                    mViewModel.expensesOperations.observe(viewLifecycleOwner, mObserver)
//                }
//            }
//            return@setOnItemSelectedListener true
//        }
//    }
}