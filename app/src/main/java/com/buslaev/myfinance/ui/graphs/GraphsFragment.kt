package com.buslaev.myfinance.ui.graphs


import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
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
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.joda.time.LocalDate

@AndroidEntryPoint
class GraphsFragment : BaseFragment<FragmentGraphsBinding>(FragmentGraphsBinding::inflate) {

    private val mViewModel by viewModels<GraphsViewModel>()

    private lateinit var mChartBalanceNavigation: BottomNavigationView
    private lateinit var mPeriodsNavigation: BottomNavigationView

    private lateinit var mObserver: Observer<List<OperationBySum>>

    private lateinit var mBarChart: BarChart

    private var balance = GENERAL_BALANCE

    private lateinit var yValuesIncome: ArrayList<BarEntry>
    private lateinit var yValuesExpenses: ArrayList<BarEntry>

    private lateinit var xValues: List<String>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupChartBalanceNavigation()
        setupPeriodsNavigation()
        setupObserver()
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
        mObserver = Observer<List<OperationBySum>> { list ->
            val resultList = if (list.isNullOrEmpty()) {
                emptyList()
            } else list
            setValues(resultList, balance)
            setupBarChart()
        }
        mViewModel.operations.observe(viewLifecycleOwner, mObserver)
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
        setXValues(list)
    }

    private fun setXValues(list: List<OperationBySum>) = runBlocking {
        val xValues = async {
            val resultList = mutableListOf<String>()
            for (i in list) {
                resultList.add(i.dateTime)
            }
            return@async resultList
        }
        this@GraphsFragment.xValues = xValues.await()
    }

    private fun setGeneralValues(list: List<OperationBySum>) = runBlocking {
        val yValuesIncome = async { getYValues(list, INCOME_BALANCE) }
        val yValuesExpenses = async { getYValues(list, EXPENSES_BALANCE) }
        this@GraphsFragment.yValuesIncome = yValuesIncome.await()
        this@GraphsFragment.yValuesExpenses = yValuesExpenses.await()
    }

    private fun setIncomeValues(list: List<OperationBySum>) = runBlocking {
        val yValuesIncome = async { getYValues(list, INCOME_BALANCE) }
        this@GraphsFragment.yValuesIncome = yValuesIncome.await()

    }

    private fun setExpensesValues(list: List<OperationBySum>) = runBlocking {
        val yValuesIncome = async { getYValues(list, INCOME_BALANCE) }
        this@GraphsFragment.yValuesIncome = yValuesIncome.await()

    }

    private fun getYValues(
        list: List<OperationBySum>,
        expectedYValues: String
    ): ArrayList<BarEntry> {
        val sortedList = if (balance == GENERAL_BALANCE) {
            list.filter { it.balance == expectedYValues }
        } else list

        var localDate = LocalDate()
        val result = ArrayList<BarEntry>()
        var xFloat = 1F
        for (i in sortedList) {
            val emptyValues = ArrayList<BarEntry>()
            while (localDate.toString() != i.dateTime) {
                emptyValues.add(BarEntry(xFloat, 0F))
                xFloat += 1F
                localDate = localDate.minusDays(1)
            }
            if (emptyValues.isNotEmpty()) {
                result.addAll(emptyValues)
            }
            localDate = localDate.minusDays(1)
            val entry = BarEntry(xFloat, i.value.toFloat())
            xFloat += 1F
            result.add(entry)
        }
        return result
    }


    private fun setupBarChart() {
        mBarChart = binding.barChart
        mBarChart.apply {
            setDrawBarShadow(false)
            setDrawValueAboveBar(true)
            //setMaxVisibleValueCount(50)
            setPinchZoom(false)
            setDrawGridBackground(false)
//            description.textAlign = Paint.Align.CENTER
//            description.text = xValues[0].substring(0, 3)
            description.isEnabled = false
            legend.form = Legend.LegendForm.CIRCLE
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        }

        val xAxis = mBarChart.xAxis
        xAxis.apply {
            valueFormatter = MyXAxisValuesFormatter()
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1F
            setCenterAxisLabels(true)
            axisMinimum = 1F
        }
        mBarChart.axisLeft.isEnabled = false
        mBarChart.axisRight.isEnabled = false

        when (balance) {
            GENERAL_BALANCE -> {
                setupDataIncomeAndExpenses()
            }
            INCOME_BALANCE -> {
                setupDataIncome()
            }
            EXPENSES_BALANCE -> {
                setupDataExpenses()
            }
        }


        mBarChart.invalidate()
    }

    class MyXAxisValuesFormatter : ValueFormatter() {
        private val localDate = LocalDate()
        private var day = localDate.minusDays(1)

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            day = day.plusDays(1)
            Log.d("xLabel", day.toString())
            return day.toString().substringAfter('-')

            // начинает с 14,а не с 15 числа
            // + нужно сделать группировку по balance
        }
    }

    private fun setupDataIncomeAndExpenses() {
        val dataSetIncome = BarDataSet(yValuesIncome, "Income")
        dataSetIncome.color = Color.GREEN
        val dataSetExpenses = BarDataSet(yValuesExpenses, "Expenses")
        dataSetExpenses.color = Color.BLUE

        val data = BarData(dataSetIncome, dataSetExpenses)

        val groupSpace = 0.4F
        val barSpace = 0F
        val barWidth = 0.3F

        mBarChart.data = data

        data.barWidth = barWidth

        mBarChart.apply {
            setScaleEnabled(false)
            groupBars(1F, groupSpace, barSpace)
        }
    }

    private fun setupDataIncome() {
        val dataSetIncome = BarDataSet(yValuesIncome, "Income")
        val data = BarData(dataSetIncome)
        mBarChart.data = data
    }

    private fun setupDataExpenses() {
        val dataSetExpenses = BarDataSet(yValuesExpenses, "Expenses")
        val data = BarData(dataSetExpenses)
        mBarChart.data = data
    }
}