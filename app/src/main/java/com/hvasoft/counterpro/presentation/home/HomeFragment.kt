package com.hvasoft.counterpro.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.hvasoft.counterpro.R
import com.hvasoft.counterpro.databinding.FragmentHomeBinding
import com.hvasoft.counterpro.domain.model.Counter
import com.hvasoft.counterpro.presentation.home.adapter.HomeAdapter
import com.hvasoft.counterpro.presentation.home.adapter.OnClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
        binding.floatingButton.setOnClickListener { createCounter() }
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(binding) {
                    homeViewModel.countersState.collectLatest { homeState ->
                        when (homeState) {
                            is HomeState.Loading -> progressBar.isVisible = true

                            is HomeState.Empty -> {
                                progressBar.isVisible = false
                                emptyStateLayout.isVisible = true
                            }

                            is HomeState.Success -> {
                                progressBar.isVisible = false
                                emptyStateLayout.isVisible = false
                                homeAdapter.submitList(homeState.counters)
                            }

                            is HomeState.Failure -> {
                                progressBar.isVisible = false
                                showErrorMessage(
                                    homeState.error?.errorMessageRes
                                        ?: R.string.error_unknown
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter(this)
        val gridLayoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.main_columns))
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = homeAdapter
        }
    }

    private fun createCounter() {
        val context = context ?: return
        val inputField = EditText(context).apply {
            hint = getString(R.string.dialog_add_counter_hint)
        }
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.dialog_add_counter_title)
            .setView(inputField)
            .setPositiveButton(R.string.dialog_add_counter_confirm) { _, _ ->
                val counterTitle = inputField.text.toString().trim()
                homeViewModel.createCounter(counterTitle)
            }
            .setNegativeButton(R.string.dialog_add_counter_cancel, null)
            .show()

    }

    private fun showErrorMessage(
        messageRes: Int,
        actionRes: Int = 0,
        actionCallback: () -> Unit = {},
    ) {
        view?.let { rootView ->
            val duration = if (actionRes != 0) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_LONG
            val snackbar = Snackbar.make(rootView, messageRes, duration)
            val params = snackbar.view.layoutParams as ViewGroup.MarginLayoutParams
            val extraBottomMargin = resources.getDimensionPixelSize(R.dimen.common_padding_default)
            params.setMargins(
                params.leftMargin,
                params.topMargin,
                params.rightMargin,
                binding.floatingButton.height + params.bottomMargin + extraBottomMargin
            )
            snackbar.view.layoutParams = params
            if (actionRes != 0) {
                snackbar.setAction(actionRes) { actionCallback.invoke() }
            }
            snackbar.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * OnClickListener
     */
    override fun onIncrementClick(counter: Counter) {
        TODO("Not yet implemented")
    }

    override fun onDecrementClick(counter: Counter) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(counter: Counter) {
        TODO("Not yet implemented")
    }
}