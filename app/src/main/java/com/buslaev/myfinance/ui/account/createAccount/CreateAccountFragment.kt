package com.buslaev.myfinance.ui.account.createAccount

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.myfinance.MainActivity
import com.buslaev.myfinance.R
import com.buslaev.myfinance.adapters.IconsAdapter
import com.buslaev.myfinance.databinding.FragmentCreateAccountBinding
import com.buslaev.myfinance.ui.BaseFragment
import com.github.dhaval2404.colorpicker.ColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CreateAccountFragment :
    BaseFragment<FragmentCreateAccountBinding>(FragmentCreateAccountBinding::inflate) {

    private val mViewModel by viewModels<CreateAccountViewModel>()

    @Inject
    lateinit var mAdapter: IconsAdapter
    private lateinit var mRecyclerView: RecyclerView

    private val account = AccountHelper()
    private val listOfIcons = listOf(
        R.drawable.ic_shopping_cart,
        R.drawable.ic_add
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).apply {
            displayTitle("Adding an account")
            hideBottomNav()
        }
        setupRecyclerView()
        setupColorPicker()
        setupAddButton()
    }

    private fun setupRecyclerView() {
        mRecyclerView = binding.iconsRecyclerView
        mRecyclerView.apply {
            adapter = mAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 4, GridLayoutManager.HORIZONTAL, false)
            itemAnimator?.changeDuration = 0
        }
        mAdapter.setList(listOfIcons)
        mAdapter.setOnItemClickListener {
            account.setIcon(it)
        }

    }

    private fun setupColorPicker() {
        binding.selectColorFab.setOnClickListener {
            ColorPickerDialog
                .Builder(requireContext())
                .setTitle(getString(R.string.default_title_color_picker))
                .setColorShape(ColorShape.CIRCLE)
                .setColorListener { colorInt, colorHex ->
                    account.setBackgroundColor(colorHex)
                    mAdapter.updateColor(colorHex)
                }
                .show()
        }
    }

    private fun setupAddButton() {
        binding.addAccountButton.setOnClickListener {
            val moneyString: String = binding.inputMoneyEditText.text.toString()
            val money: Double = if (moneyString.isEmpty()) 0.0 else moneyString.toDouble()
            val currency = binding.currencyTextView.text.toString()
            val title = binding.inputNameAccountEditText.text.toString()
            account.apply {
                setMoney(money)
                setCurrency(currency)
                setTitle(title)
            }
            if (account.isCorrectAttributes()) {
                val currentAccount = account.getAccount()
                mViewModel.addAccount(account = currentAccount)
                (activity as MainActivity).navController.navigate(R.id.action_createAccountFragment_to_accountFragment)
            } else {
                Toast.makeText(requireContext(), "Please check all attributes", Toast.LENGTH_SHORT)
                    .show()
            }

        }
    }
}