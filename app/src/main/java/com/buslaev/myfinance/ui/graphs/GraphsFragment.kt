package com.buslaev.myfinance.ui.graphs


import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.buslaev.myfinance.R
import com.buslaev.myfinance.databinding.FragmentGraphsBinding
import com.buslaev.myfinance.entities.OperationBySum
import com.buslaev.myfinance.other.Constants.EXPENSES_BALANCE
import com.buslaev.myfinance.other.Constants.GENERAL_BALANCE
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.buslaev.myfinance.other.EnumHelper.BarDates.*
import com.buslaev.myfinance.ui.BaseFragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking


class GraphsFragment : BaseFragment<FragmentGraphsBinding>(FragmentGraphsBinding::inflate) {

    private val mViewModel by viewModels<GraphsViewModel>()

    private lateinit var mChartBalanceNavigation: BottomNavigationView
    private lateinit var mPeriodsNavigation: BottomNavigationView

    private lateinit var mObserverIncome: Observer<List<OperationBySum>>
    private lateinit var mObserverExpenses: Observer<List<OperationBySum>>

    private lateinit var mBarChart: BarChart

    private var balance = GENERAL_BALANCE

    private lateinit var yValuesIncome: ArrayList<BarEntry>
    private lateinit var yValuesExpenses: ArrayList<BarEntry>

    private lateinit var xValues: List<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChartBalanceNavigation()
        setupPeriodsNavigation()
        setupObserverIncome()
        setupBarChart()
    }

    private fun setupChartBalanceNavigation() {
        mChartBalanceNavigation = binding.chartBalanceNavigationView
        mChartBalanceNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.general_chart_menu -> {
                    balance = GENERAL_BALANCE
                }
                R.id.income_chart_menu -> {
                    balance = INCOME_BALANCE
                }
                R.id.expenses_chart_menu -> {
                    balance = EXPENSES_BALANCE
                }
            }
            setupObserver()
            return@setOnItemSelectedListener true
        }
    }

    private fun setupPeriodsNavigation() {
        mPeriodsNavigation = binding.periodsNavigationView
        mPeriodsNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.chart_on_days -> {
                    mViewModel.getOperations(ON_DAYS, balance)
                }
                R.id.chart_on_weeks -> {
                    mViewModel.getOperations(ON_WEEK, balance)
                }
                R.id.chart_on_months -> {
                    mViewModel.getOperations(ON_MONTH, balance)
                }
                R.id.chart_on_years -> {
                    mViewModel.getOperations(ON_YEARS, balance)
                }
            }
            setupObserver()
            return@setOnItemSelectedListener true
        }
    }

    private fun setupObserver() {
        when (balance) {
            GENERAL_BALANCE -> {
                setupObserverIncome()
                setupObserverExpenses()
            }
            INCOME_BALANCE -> {
                setupObserverIncome()
            }
            EXPENSES_BALANCE -> {
                setupObserverExpenses()
            }
        }
    }

    private fun setupObserverIncome() {
        mObserverIncome = Observer { list ->
            val resultList = if (list.isNullOrEmpty()) {
                emptyList()
            } else list
            setValues(resultList, balance)
            setupBarChart()
        }
        when (balance) {
            GENERAL_BALANCE -> {
            }
            INCOME_BALANCE -> {
            }
            EXPENSES_BALANCE -> {
            }
        }
    }

    private fun setupObserverExpenses() {
        mObserverExpenses = Observer { list ->
            val resultList = if (list.isNullOrEmpty()) {
                emptyList()
            } else list
            setValues(resultList, balance)
            setupBarChart()
        }
        when (balance) {
            GENERAL_BALANCE -> {
            }
            INCOME_BALANCE -> {
            }
            EXPENSES_BALANCE -> {
            }
        }
    }

    private fun setValues(list: List<OperationBySum>, balance: String) {
        when (balance) {
            GENERAL_BALANCE -> {
                setGeneralValues(list)
            }
            INCOME_BALANCE -> {
                setIncomeValues(list)
            }
            EXPENSES_BALANCE -> {
                setExpensesValues(list)
            }
        }
    }

    private fun setGeneralValues(list: List<OperationBySum>) = runBlocking {
        val yValuesIncome = async { getYValues(list) }
        this@GraphsFragment.yValuesIncome = yValuesIncome.await()
    }

    private fun setIncomeValues(list: List<OperationBySum>) {

    }

    private fun setExpensesValues(list: List<OperationBySum>) {

    }

    private fun getYValues(list: List<OperationBySum>): ArrayList<BarEntry> {
        val result = ArrayList<BarEntry>()
        var xFloat = 1F
        for (i in list) {
            val entry = BarEntry(xFloat, i.value.toFloat())
            xFloat++
            result.add(entry)
        }
        return result
    }


    private fun setupBarChart() {
        mBarChart = binding.barChart
        mBarChart.apply {
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            setMaxVisibleValueCount(50)
            setPinchZoom(false)
            setDrawGridBackground(false)
        }

        val dataSetIncome = BarDataSet(yValuesIncome, "Income")
        val dataSetExpenses = BarDataSet(yValuesExpenses, "Expenses")

        val data = BarData(dataSetIncome, dataSetExpenses)

        val groupSpace = 0.1F
        val barSpace = 0.02F
        val barWidth = 0.42F

        mBarChart.data = data

        data.barWidth = barWidth

        mBarChart.apply {
            groupBars(1F, groupSpace, barSpace)
        }


        val xAxis = mBarChart.xAxis
        xAxis.apply {
            valueFormatter = MyXAxisValuesFormatter(xValues)
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1F
            setCenterAxisLabels(true)
            axisMinimum = 1F
        }
        mBarChart.invalidate()
    }

    class MyXAxisValuesFormatter(private val list: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float, axis: AxisBase?): String {
            return list[(value.toInt())]
        }
    }
}