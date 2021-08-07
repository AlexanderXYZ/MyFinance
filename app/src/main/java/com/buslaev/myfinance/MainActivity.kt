package com.buslaev.myfinance

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    lateinit var bottomNavView: CoordinatorLayout
    lateinit var floatingActionButton: FloatingActionButton

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this
        setupDependency()
        setupNavigation()
    }

    private fun setupNavigation() {
        mBinding.bottomNavView.apply {
            background = null
            setupWithNavController(
                    Navigation.findNavController(
                            this@MainActivity,
                            R.id.nav_host_fragment
                    )
            )
        }
    }

    private fun setupDependency() {
        bottomNavView = mBinding.bottomNavViewCoordinator
        floatingActionButton = mBinding.fab
        navController = findNavController(R.id.nav_host_fragment)
    }
}