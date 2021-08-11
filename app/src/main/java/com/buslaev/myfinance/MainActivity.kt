package com.buslaev.myfinance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setupDependency()
        setupNavigation()
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
}