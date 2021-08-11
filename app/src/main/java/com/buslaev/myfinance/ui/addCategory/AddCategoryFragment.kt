package com.buslaev.myfinance.ui.addCategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.buslaev.myfinance.MainActivity
import com.buslaev.myfinance.R
import com.buslaev.myfinance.adapters.IconsAdapter
import com.buslaev.myfinance.databinding.FragmentAddCategoryBinding
import com.buslaev.myfinance.entities.Categories
import com.buslaev.myfinance.other.Constants.EXPENSES_BALANCE
import com.buslaev.myfinance.other.Constants.INCOME_BALANCE
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.takusemba.multisnaprecyclerview.MultiSnapRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddCategoryFragment : Fragment() {

    private var _binding: FragmentAddCategoryBinding? = null
    private val mBinding get() = _binding!!

    private val mViewModel by viewModels<AddCategoryViewModel>()

    @Inject
    lateinit var mAdapter: IconsAdapter
    private lateinit var mRecyclerView: MultiSnapRecyclerView

    private var balanceSelected: String = ""
    private var backgroundColorSelected: String = "#" + Integer.toHexString(R.color.dark_blue)
    private var iconSelected: Int? = null

    private val testList = listOf(
        R.drawable.ic_shopping_cart,
        R.drawable.ic_add
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddCategoryBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).apply {
            displayTitle("Create category")
            hideBottomNav()
        }

        initBalanceRadioGroup()
        initViewPagerIcons()
        initColorPickerButton()
        initAddButton()
    }

    private fun initBalanceRadioGroup() {
        mBinding.balanceRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.income_radioButton -> {
                    balanceSelected = INCOME_BALANCE
                }
                R.id.expanse_radioButton -> {
                    balanceSelected = EXPENSES_BALANCE
                }
            }
        }
    }

    private fun initViewPagerIcons() {
        mAdapter.setList(testList)
        mRecyclerView = mBinding.multiRecyclerView
        mRecyclerView.apply {
            adapter = mAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 3, GridLayoutManager.HORIZONTAL, false)
            itemAnimator?.changeDuration = 0
        }
        mAdapter.setOnItemClickListener {
            iconSelected = it
        }
    }

    private fun initColorPickerButton() {
        mBinding.addColorFab.setOnClickListener {
            ColorPickerDialog
                .Builder(requireContext())
                .setTitle(getString(R.string.default_title_color_picker))
                .setColorShape(ColorShape.CIRCLE)
                .setColorListener { color, colorHex ->
                    backgroundColorSelected = colorHex
                    mAdapter.updateColor(backgroundColorSelected)
                }
                .show()
        }
    }

    private fun initAddButton() {
        mBinding.addCategoryButton.setOnClickListener {
            val title = mBinding.categoryTitleEditText.text.toString()
            val balance = balanceSelected
            val backgroundColor = backgroundColorSelected
            val icon = iconSelected
            if (title.isEmpty() || balance.isEmpty() || backgroundColor.isEmpty() || icon == null) {
                Toast.makeText(requireContext(), "Please input all attributes", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val category = Categories(
                    title = title,
                    icon = icon,
                    backgroundColor = backgroundColor,
                    balance = balance
                )
                mViewModel.addCategory(category)
                (activity as MainActivity).navController.navigate(R.id.action_addCategoryFragment_to_categoriesFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}