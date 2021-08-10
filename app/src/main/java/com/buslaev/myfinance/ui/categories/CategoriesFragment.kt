package com.buslaev.myfinance.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buslaev.myfinance.MainActivity
import com.buslaev.myfinance.R
import com.buslaev.myfinance.adapters.CategoriesAdapter
import com.buslaev.myfinance.databinding.FragmentCategoriesBinding
import com.buslaev.myfinance.entities.Categories
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val mBinding get() = _binding!!

    private val mViewModel by viewModels<CategoriesViewModel>()

    @Inject
    lateinit var mAdapter: CategoriesAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mObserver: Observer<List<Categories>>

    private val numberOfColumns = 4


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddButton()
        setupRecyclerView()
    }

    private fun setupAddButton() {
        (activity as MainActivity).floatingActionButton.setOnClickListener {
            (activity as MainActivity).navController.navigate(R.id.action_categoriesFragment_to_addCategoryFragment)
        }
    }

    private fun setupRecyclerView() {
        mRecyclerView = mBinding.categoriesRecyclerView
        mRecyclerView.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        }
        mObserver = Observer { list ->
            mAdapter.setList(list)
        }
        mViewModel.categories.observe(viewLifecycleOwner, mObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}