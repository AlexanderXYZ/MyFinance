package com.buslaev.myfinance.ui.addOperation

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.myfinance.MainActivity
import com.buslaev.myfinance.R
import com.buslaev.myfinance.adapters.CategoriesAdapter
import com.buslaev.myfinance.databinding.FragmentAddOperationBinding
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.other.Constants.COLOR_SELECTED_DATE
import com.buslaev.myfinance.other.Constants.COLOR_TRANSPARENT
import com.buslaev.myfinance.other.Constants.EXPENSES_BALANCE
import com.buslaev.myfinance.other.Constants.FRAGMENT_MAIN
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.buslaev.myfinance.other.Constants.PARENT_FRAGMENT_KEY
import com.buslaev.myfinance.ui.BaseFragment
import com.buslaev.myfinance.utilits.CorrectValues
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.LocalDate
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AddOperationFragment :
    BaseFragment<FragmentAddOperationBinding>(FragmentAddOperationBinding::inflate),
    DatePickerDialog.OnDateSetListener {


    private val mViewModel by viewModels<AddOperationViewModel>()

    @Inject
    lateinit var mAdapter: CategoriesAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mObserver: Observer<List<Categories>>

    private lateinit var mBalanceNavigation: BottomNavigationView

    private val correctValues = CorrectValues()

    private var value: Double = 0.0
    private var account: String = "main"
    private var dateTime: String = ""
    private var balance: String = INCOME_BALANCE
    private var idCategory: Int = 0

    private var parentFragment: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragment = arguments?.getString(PARENT_FRAGMENT_KEY, "").toString()
        hideBottomView()
        setupBalanceNavigation()
        setupCategoriesRecyclerView()
        setupObserver(INCOME_BALANCE)
        setupDates()
        setupAddButton()
    }

    private fun hideBottomView() {
        (activity as MainActivity).apply {
            displayTitle("Adding an operation")
            hideBottomNav()
        }
    }

    private fun setupBalanceNavigation() {
        mBalanceNavigation = binding.balanceNavigationView
        underlineMenuItem(mBalanceNavigation.menu.getItem(0))
        mBalanceNavigation.setOnItemSelectedListener {
            removeItemsUnderline(mBalanceNavigation.menu)
            underlineMenuItem(it)
            when (it.itemId) {
                R.id.income_menu -> {
                    balance = INCOME_BALANCE
                }
                R.id.expenses_menu -> {
                    balance = EXPENSES_BALANCE
                }
            }
            setupObserver(balance)
            return@setOnItemSelectedListener true
        }
    }

    private fun setupCategoriesRecyclerView() {
        mRecyclerView = binding.categoriesRecyclerView
        mRecyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireContext(), 4)
            itemAnimator?.changeDuration = 0
        }
        mAdapter.setOnItemClickListener {
            idCategory = it
        }
    }

    private fun setupObserver(balance: String) {
        mObserver = Observer { list ->
            mAdapter.setList(list)
        }
        if (balance == INCOME_BALANCE) {
            mViewModel.categoriesIncome.observe(viewLifecycleOwner, mObserver)
        } else {
            mViewModel.categoriesExpenses.observe(viewLifecycleOwner, mObserver)
        }
    }

    private fun setupDates() {
        val localDate = LocalDate()
        val today = localDate.toString()
        val yesterday = localDate.minusDays(1).toString()
        binding.todayDateTextView.apply {
            text = "${today.substringAfter('-')}\ntoday"
            setOnClickListener {
                setBackgroundsOfDates(it)
                dateTime = today
            }
        }
        binding.yesterdayDateTextView.apply {
            text = "${yesterday.substringAfter('-')}\nyesterday"
            setOnClickListener {
                setBackgroundsOfDates(it)
                dateTime = yesterday
            }
        }
        binding.selectedDateTextView.text = "${today.substringAfter('-')}\nselected"
        binding.selectDateFab.setOnClickListener {
            showDatePickerDialog()
            setBackgroundsOfDates(binding.selectedDateTextView)
        }
    }

    private fun setBackgroundsOfDates(it: View?) {
        val defaultColor = Color.parseColor(COLOR_TRANSPARENT)
        val selectedColor = Color.parseColor(COLOR_SELECTED_DATE)
        binding.apply {
            todayDateTextView.setBackgroundColor(defaultColor)
            yesterdayDateTextView.setBackgroundColor(defaultColor)
            selectedDateTextView.setBackgroundColor(defaultColor)
        }
        it?.setBackgroundColor(selectedColor)
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            this,
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val monthString = addZeroFrontDate(month + 1)
        val dayOfMonthString = addZeroFrontDate(dayOfMonth)
        val date = "$year-$monthString-$dayOfMonthString"
        dateTime = date
        binding.selectedDateTextView.text = "${date.substringAfter('-')}\nselected"
    }

    private fun addZeroFrontDate(date: Int): String = if (date < 10) "0$date" else date.toString()

    private fun setupAddButton() {
        binding.addOperationButton.setOnClickListener {
            setupAttributes()
            val isCorrect =
                correctValues.checkOperation(value, account, dateTime, balance, idCategory)
            if (isCorrect) {
                val operation = correctValues.getCorrectOperation()
                mViewModel.addOperation(operation)
                when (balance) {
                    INCOME_BALANCE -> mViewModel.changeValueOfAccountIncome(value, account)
                    EXPENSES_BALANCE -> mViewModel.changeValueOfAccountExpenses(value, account)
                }
                navigateBack()
            } else {
                Toast.makeText(requireContext(), "Please check all attributes", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupAttributes() {
        val stringValue = binding.inputMoneyEditText.text.toString()
        value = if (stringValue.isEmpty()) 0.0 else stringValue.toDouble()
    }

    private fun navigateBack() {
        if (parentFragment == FRAGMENT_MAIN) {
            (activity as MainActivity).navController.navigate(R.id.action_addOperationFragment_to_mainFragment)
        } else {
            (activity as MainActivity).navController.navigate(R.id.action_addOperationFragment_to_operationsFragment)
        }
    }
}