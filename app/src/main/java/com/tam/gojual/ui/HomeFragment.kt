package com.tam.gojual.ui // Sesuaikan dengan package Anda

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tam.gojual.R
import com.tam.gojual.databinding.FragmentHomeBinding
import com.tam.gojual.ui.adapter.PostAdapter
import com.tam.gojual.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    // Properti untuk ViewBinding agar aman digunakan dalam Fragment
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Inisialisasi ViewModel dan Adapter
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Panggil semua fungsi setup
        setupRecyclerView()
        observeViewModel()
        setupMenu()
    }

    /**
     * Menyiapkan menu pencarian dan filter di toolbar.
     */
    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Tampilkan menu dari file home_menu.xml
                menuInflater.inflate(R.menu.home_menu, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView

                // Atur listener untuk search view
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        viewModel.searchPosts(query)
                        searchView.clearFocus()
                        return true
                    }
                    override fun onQueryTextChange(newText: String?): Boolean = false
                })

                // Atur listener untuk mereset pencarian saat search view ditutup
                searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                    override fun onMenuItemActionExpand(item: MenuItem): Boolean = true
                    override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                        viewModel.searchPosts(null)
                        return true
                    }
                })
            }

            // Fungsi ini akan menangani klik pada item menu yang bukan action view (seperti filter)
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_filter -> {
                        // Saat ikon filter diklik, tampilkan BottomSheetDialog
                        FilterBottomSheetFragment().show(childFragmentManager, "FilterBottomSheet")
                        true // Return true karena kita sudah menangani klik ini
                    }
                    else -> false // Biarkan sistem yang menangani klik lain
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    /**
     * Menyiapkan RecyclerView, Adapter, dan listener untuk scroll paginasi.
     */
    private fun setupRecyclerView() {
        postAdapter = PostAdapter { post ->
            // Aksi saat item di-klik: Buka halaman detail dengan mengirim ID post
            val intent = Intent(requireActivity(), ItemDetailActivity::class.java).apply {
                putExtra("POST_ID", post.id)
            }
            startActivity(intent)
        }

        binding.rvPosts.apply {
            adapter = postAdapter
            layoutManager = LinearLayoutManager(requireContext())
            // Listener untuk mendeteksi scroll hingga ke bawah untuk paginasi
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
                    val totalItemCount = layoutManager.itemCount

                    if (totalItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1) {
                        viewModel.loadNextPage()
                    }
                }
            })
        }
    }

    /**
     * Mengamati perubahan data dari ViewModel dan mengupdate UI.
     */
    private fun observeViewModel() {
        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts)
            handleEmptyState(posts.isEmpty() && viewModel.isLoading.value == false)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading && postAdapter.itemCount == 0) View.VISIBLE else View.GONE
            if (!isLoading) {
                handleEmptyState(postAdapter.itemCount == 0)
            }
        }
    }

    /**
     * Fungsi pembantu untuk mengelola visibilitas tampilan kosong.
     */
    private fun handleEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.tvEmptyState.visibility = View.VISIBLE
            binding.rvPosts.visibility = View.GONE
        } else {
            binding.tvEmptyState.visibility = View.GONE
            binding.rvPosts.visibility = View.VISIBLE
        }
    }

    /**
     * Membersihkan binding saat view dihancurkan untuk menghindari memory leak.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}