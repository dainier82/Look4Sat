package com.rtbishop.look4sat.ui.mainScreen

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rtbishop.look4sat.R
import com.rtbishop.look4sat.SharedViewModel
import com.rtbishop.look4sat.data.SatEntry
import com.rtbishop.look4sat.databinding.FragmentEntriesBinding
import com.rtbishop.look4sat.ui.adapters.EntriesAdapter
import com.rtbishop.look4sat.utility.RecyclerDivider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntriesFragment : Fragment(R.layout.fragment_entries) {

    private var binding: FragmentEntriesBinding? = null
    private var entriesAdapter: EntriesAdapter? = null
    private val viewModel: SharedViewModel by activityViewModels()
    private val pickFileReqCode = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEntriesBinding.bind(view)
        setupComponents()
        observeEntries()
    }

    private fun setupComponents() {
        entriesAdapter = EntriesAdapter()
        binding?.apply {
            entriesRecycler.apply {
                setHasFixedSize(true)
                adapter = entriesAdapter
                layoutManager = LinearLayoutManager(requireContext())
                addItemDecoration(RecyclerDivider(R.drawable.rec_divider_light))
            }
            importWeb.setOnClickListener { showImportFromWebDialog() }
            importFile.setOnClickListener { showImportFromFileDialog() }
            selectAll.setOnClickListener { entriesAdapter?.selectAll() }
            entriesFab.setOnClickListener { navigateToPasses() }
            searchBar.setOnQueryTextListener(entriesAdapter)
            searchBar.clearFocus()
        }
    }

    private fun observeEntries() {
        viewModel.getEntries().observe(viewLifecycleOwner, { entries ->
            if (entries.isNullOrEmpty()) setError()
            else {
                viewModel.setEntries(entries)
                entriesAdapter?.setEntries(entries as MutableList<SatEntry>)
                setLoaded()
            }
            observeEvents()
        })
    }

    private fun observeEvents() {
        viewModel.getAppEvent().observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandled()?.let {
                if (it == 0) setLoading()
                else if (it == 1) {
                    val errorMsg = getString(R.string.entries_update_error)
                    Snackbar.make(requireView(), errorMsg, Snackbar.LENGTH_SHORT).show()
                    setError()
                }
            }
        })
    }

    private fun showImportFromWebDialog() {
        findNavController().navigate(R.id.action_entries_to_dialog_sources)
    }

    private fun showImportFromFileDialog() {
        Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            startActivityForResult(this, pickFileReqCode)
        }
    }

    private fun setLoaded() {
        binding?.apply {
            entriesError.visibility = View.INVISIBLE
            entriesProgress.visibility = View.INVISIBLE
            entriesRecycler.visibility = View.VISIBLE
        }
    }

    private fun setLoading() {
        binding?.apply {
            entriesError.visibility = View.INVISIBLE
            entriesRecycler.visibility = View.INVISIBLE
            entriesProgress.visibility = View.VISIBLE
        }
    }

    private fun setError() {
        binding?.apply {
            entriesProgress.visibility = View.INVISIBLE
            entriesRecycler.visibility = View.INVISIBLE
            entriesError.visibility = View.VISIBLE
        }
    }

    private fun navigateToPasses() {
        binding?.searchBar?.clearFocus()
        entriesAdapter?.let { viewModel.updateEntriesSelection(it.getEntries()) }
        requireView().findNavController().navigate(R.id.action_entries_to_passes)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == pickFileReqCode && resultCode == AppCompatActivity.RESULT_OK) {
            data?.data?.also { uri -> viewModel.updateEntriesFromFile(uri) }
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroyView() {
        entriesAdapter = null
        binding = null
        super.onDestroyView()
    }
}