package com.hvasoft.counterpro.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
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

    private fun setupRecyclerView() {
        homeAdapter = HomeAdapter(this)
        val gridLayoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.main_columns))
        binding.recyclerView.apply {
            layoutManager = gridLayoutManager
            adapter = homeAdapter
        }

        homeAdapter.isActionModeEnabled.observe(viewLifecycleOwner) { isActionModeEnabled ->
            if (isActionModeEnabled) setupToolbarMenu(true)
            else setupToolbarMenu(false)
        }
    }

    private fun setupViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(binding) {
                    homeViewModel.countersState.collectLatest { homeState ->
                        when (homeState) {
                            is HomeState.Loading -> progressBar.isVisible = true

                            is HomeState.Empty -> {
                                homeAdapter.submitList(null)
                                progressBar.isVisible = false
                                emptyStateLayout.isVisible = true
                            }

                            is HomeState.Success -> {
                                progressBar.isVisible = false
                                emptyStateLayout.isVisible = false
                                homeAdapter.submitList(null)
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
                homeAdapter.disableActionMode()
            }
            .setNegativeButton(R.string.dialog_counter_cancel, null)
            .show()
    }

    private fun showErrorMessage(messageRes: Int) {
        view?.let { rootView ->
            val snackBar = Snackbar.make(rootView, messageRes, Snackbar.LENGTH_LONG)
            val params = snackBar.view.layoutParams as ViewGroup.MarginLayoutParams
            val extraBottomMargin = resources.getDimensionPixelSize(R.dimen.common_padding_default)
            params.setMargins(
                params.leftMargin,
                params.topMargin,
                params.rightMargin,
                binding.floatingButton.height + params.bottomMargin + extraBottomMargin
            )
            snackBar.view.layoutParams = params
            snackBar.show()
        }
    }

    private fun setupToolbarMenu(isActionModeEnabled: Boolean) {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {

            override fun onPrepareMenu(menu: Menu) {
                val btnDeselect = menu.findItem(R.id.btnDeselect)
                if (btnDeselect != null) btnDeselect.isVisible = isActionModeEnabled
                val btnDelete = menu.findItem(R.id.btnDelete)
                if (btnDelete != null) btnDelete.isVisible = isActionModeEnabled
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                if (!menu.hasVisibleItems()) menuInflater.inflate(R.menu.counter_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.btnDeselect -> {
                        homeAdapter.disableActionMode()
                    }
                    R.id.btnDelete -> {
                        val selectedCounters = homeAdapter.currentList.filter { it.isSelected }
                        confirmDelete(selectedCounters)
                    }
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun confirmDelete(counters: List<Counter>) {
        val context = context ?: return
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.dialog_delete_counter_title)
            .setPositiveButton(R.string.text_btn_delete) { _, _ ->
                homeViewModel.deleteCounters(counters)
                homeAdapter.disableActionMode()
            }
            .setNegativeButton(R.string.dialog_counter_cancel, null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * OnClickListener
     */
    override fun onIncrementClick(counter: Counter) {
        homeViewModel.incrementCounter(counter)
    }

    override fun onDecrementClick(counter: Counter) {
        homeViewModel.decrementCounter(counter)
    }
}