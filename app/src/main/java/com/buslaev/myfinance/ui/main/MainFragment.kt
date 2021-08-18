package com.buslaev.myfinance.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.*
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
import com.buslaev.myfinance.entities.OperationBySum
import com.buslaev.myfinance.other.Constants.EXPENSES_BALANCE
import com.buslaev.myfinance.other.Constants.FRAGMENT_MAIN
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.buslaev.myfinance.other.Constants.MAIN_BACKGROUND_WHIT
import com.buslaev.myfinance.other.Constants.PARENT_FRAGMENT_KEY
import com.buslaev.myfinance.ui.BaseFragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment :
    BaseFragment<FragmentMainBinding>(FragmentMainBinding::inflate) {

    private val mViewModel by viewModels<MainViewModel>()

    @Inject
    lateinit var mAdapter: MainAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mObserver: Observer<List<OperationBySum>>
    private lateinit var mObserverTitle: Observer<String>

    private lateinit var mBalanceNavigation: BottomNavigationView
    private lateinit var mPeriodsNavigation: BottomNavigationView

    private lateinit var mChart: PieChart
    private lateinit var yValues: ArrayList<PieEntry>
    private lateinit var yColors: ArrayList<Int>
    private var centerText: Double = 0.0

    private var balance = INCOME_BALANCE

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).titleInCenter()
        setupBalanceNavigation()
        setupPeriodsNavigation()
        setupRecyclerView()
        setupObserver(balance)
        setupObserverTitle()
    }

    private fun setupObserverTitle() {
        mObserverTitle = Observer {
            (activity as MainActivity).setTitleInCenter(it)
        }
        mViewModel.title.observe(viewLifecycleOwner, mObserverTitle)
    }

    override fun onStart() {
        super.onStart()
        initFields()
    }

    private fun initFields() {
        (activity as MainActivity).floatingActionButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(PARENT_FRAGMENT_KEY, FRAGMENT_MAIN)
            (activity as MainActivity).navController.navigate(
                R.id.action_mainFragment_to_addOperationFragment,
                bundle
            )
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

    private fun setupPeriodsNavigation() {
        mPeriodsNavigation = binding.periodsNavigationView
        mPeriodsNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.day_menu -> {
                    mViewModel.getOperationsByDay(balance)
                }
                R.id.weak_menu -> {
                    mViewModel.getOperationsByWeek(balance)
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
            setupObserver(balance)
            return@setOnItemSelectedListener true
        }
    }

    private fun setupRecyclerView() {
        mRecyclerView = binding.mainRecyclerView
        mRecyclerView.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupObserver(balance: String) {
        mObserver = Observer { list ->
            val resultList = if (list.isNullOrEmpty()) {
                emptyList()
            } else {
                list
            }
            setValues(resultList)
            mAdapter.setList(resultList)
            setupChart()
        }
        if (balance == INCOME_BALANCE) {
            mViewModel.incomeOperations.observe(viewLifecycleOwner, mObserver)
        } else {
            mViewModel.expensesOperations.observe(viewLifecycleOwner, mObserver)
        }

    }

    private fun setValues(resultList: List<OperationBySum>) = runBlocking {
        val yValues = async { setYValues(resultList) }
        val yColors = async { setYColors(resultList) }
        this@MainFragment.yValues = yValues.await()
        this@MainFragment.yColors = yColors.await()
    }

    private fun setYColors(resultList: List<OperationBySum>): ArrayList<Int> {
        val yColors = ArrayList<Int>()
        for (i in resultList) {
            val color = Color.parseColor(i.backgroundColor)
            yColors.add(color)
        }
        return yColors
    }

    private fun setYValues(resultList: List<OperationBySum>): ArrayList<PieEntry> {
        val yValues = ArrayList<PieEntry>()
        for (i in resultList) {
            val pieEntry = PieEntry(i.value.toFloat(), i.titleCategory)
            yValues.add(pieEntry)
            centerText += i.value
        }
        return yValues
    }

    private fun setupChart() {
        mChart = binding.pieChart
        mChart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            isDrawHoleEnabled = true
            //setHoleColor(Color.parseColor(MAIN_BACKGROUND_WHIT))
            transparentCircleRadius = 0F
            centerText = this@MainFragment.centerText.toString().substringBefore('.')
            setDrawCenterText(true)
            setCenterTextColor(Color.BLACK)
            setCenterTextSize(20F)
            setEntryLabelColor(Color.BLACK)
            animateY(1000, Easing.EaseInOutCubic)
            legend.isEnabled = false
        }

        val dataSet = PieDataSet(yValues, balance)
        dataSet.apply {
            sliceSpace = 3F
            selectionShift = 5F
            colors = yColors
        }

        val data = PieData(dataSet)
        data.apply {
            setDrawValues(false)

        }
        mChart.data = data
        mChart.invalidate()
        centerText = 0.0
    }
}