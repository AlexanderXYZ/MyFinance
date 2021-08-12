package com.buslaev.myfinance.ui.addOperation

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
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
import com.buslaev.myfinance.utilits.CorrectValues
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import org.joda.time.LocalDate
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class AddOperationFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private var _binding: FragmentAddOperationBinding? = null
    private val mBinding get() = _binding!!

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddOperationBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

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
            hideBottomNav()
            displayTitle("Adding an operation")
        }
    }

    private fun setupBalanceNavigation() {
        mBalanceNavigation = mBinding.balanceNavigationView
        mBalanceNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.income_menu -> {
                    setupObserver(INCOME_BALANCE)
                    balance = INCOME_BALANCE
                }
                R.id.expenses_menu -> {
                    setupObserver(EXPENSES_BALANCE)
                    balance = EXPENSES_BALANCE
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun setupCategoriesRecyclerView() {
        mRecyclerView = mBinding.categoriesRecyclerView
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
        mBinding.todayDateTextView.apply {
            text = "${today.substringAfter('-')}\ntoday"
            setOnClickListener {
                setBackgroundsOfDates(it)
                dateTime = today
            }
        }
        mBinding.yesterdayDateTextView.apply {
            text = "${yesterday.substringAfter('-')}\nyesterday"
            setOnClickListener {
                setBackgroundsOfDates(it)
                dateTime = yesterday
            }
        }
        mBinding.selectedDateTextView.text = "${today.substringAfter('-')}\nselected"
        mBinding.selectDateFab.setOnClickListener {
            showDatePickerDialog()
            setBackgroundsOfDates(mBinding.selectedDateTextView)
        }
    }

    private fun setBackgroundsOfDates(it: View?) {
        val defaultColor = Color.parseColor(COLOR_TRANSPARENT)
        val selectedColor = Color.parseColor(COLOR_SELECTED_DATE)
        mBinding.apply {
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
        val date = "$year-$month-$dayOfMonth"
        dateTime = date
        mBinding.selectedDateTextView.text = "${date.substringAfter('-')}\nselected"
    }

    private fun setupAddButton() {
        mBinding.addOperationButton.setOnClickListener {
            setupAttributes()
            val isCorrect =
                correctValues.checkOperation(value, account, dateTime, balance, idCategory)
            if (isCorrect) {
                val operation = correctValues.getCorrectOperation()
                mViewModel.addOperation(operation)
                navigateBack()
            } else {
                Toast.makeText(requireContext(), "Please check all attributes", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun setupAttributes() {
        val stringValue = mBinding.inputMoneyEditText.text.toString()
        value = if (stringValue.isEmpty()) 0.0 else stringValue.toDouble()
    }

    private fun navigateBack() {
        if (parentFragment == FRAGMENT_MAIN) {
            (activity as MainActivity).navController.navigate(R.id.action_addOperationFragment_to_mainFragment)
        } else {
            (activity as MainActivity).navController.navigate(R.id.action_addOperationFragment_to_operationsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}