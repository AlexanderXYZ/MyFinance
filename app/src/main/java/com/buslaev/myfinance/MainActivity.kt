package com.buslaev.myfinance

import android.content.DialogInterface
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.children
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.buslaev.myfinance.databinding.ActivityMainBinding
import com.buslaev.myfinance.other.Constants.APP_ACTIVITY
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!

    private lateinit var bottomNavView: CoordinatorLayout
    lateinit var floatingActionButton: FloatingActionButton

    lateinit var navController: NavController

    lateinit var title: TextView
    lateinit var account: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setupDependency()
        setupNavigation()
        supportActionBar?.elevation = 0F
    }

    private fun setupDependency() {
        bottomNavView = mBinding.bottomNavViewCoordinator
        floatingActionButton = mBinding.fab
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.nav_host_fragment)
        mBinding.bottomNavView.apply {
            background = null
            setupWithNavController(navController)
        }
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.mainFragment -> {
                    showBottomNav()
                }
                R.id.operationsFragment -> {
                    showBottomNav()
                }
                R.id.graphsFragment -> {
                    showBottomNav()
                }
                R.id.categoriesFragment -> {
                    showBottomNav()
                }
            }
        }
    }

    private fun showBottomNav() {
        bottomNavView.visibility = View.VISIBLE
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    fun hideBottomNav() {
        bottomNavView.visibility = View.GONE
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun displayTitle(dTitle: String) {
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_TITLE
            title = dTitle
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    fun titleInCenter() {
        supportActionBar?.apply {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            val view = layoutInflater.inflate(R.layout.action_bar_layout, null)
            val params = ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
            this@MainActivity.title = view.findViewById(R.id.title_textView_actionBar)
            this@MainActivity.account = view.findViewById(R.id.account_ll_actionBar)
            setupAccountClick()
            setCustomView(view, params)
        }
    }

    private fun setupAccountClick() {
        account.setOnClickListener {
            showAlertDialogAccount()
        }
    }

    private fun showAlertDialogAccount() {
        val options = arrayOf(
            "Relevance",
            "Price - Low to High",
            "Price - High to Low",
            "Newest"
        )

        val alertDialog = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.dialog_chose_account, null)
        val radioGroup = view.findViewById<RadioGroup>(R.id.dialog_radioGroup)

        val radioStyle = ContextThemeWrapper(radioGroup.context, R.style.MyRadioButton)
        for (option in options) {
            val radioButton = RadioButton(radioStyle)
            val drawable = applicationContext.resources.getDrawable(R.drawable.ic_categoris)
            drawable.setTint(Color.BLACK)
            val params = RadioGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(30, 30, 0, 30)
            radioButton.apply {
                layoutParams = params
                text = "   $option (15 000)"
                setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            }
            radioGroup.addView(radioButton)
        }
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            for (child in radioGroup.children) {
                child as RadioButton
                if (child.id == checkedId) {
                    child.setTextColor(Color.BLUE)
                } else {
                    child.setTextColor(Color.BLACK)
                }
            }
            Toast.makeText(applicationContext, "id = ${options[checkedId - 1]}", Toast.LENGTH_SHORT)
                .show()
        }
        alertDialog.setView(view)
        alertDialog.show()
    }

    fun setTitleInCenter(inputTitle: String) {
        title.text = inputTitle
    }

}