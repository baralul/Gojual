package com.tam.gojual.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.tam.gojual.data.model.Post
import com.tam.gojual.data.repository.PostRepository
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>> = _posts

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private var lastVisiblePost: DocumentSnapshot? = null
    private var isLastPage = false
    private var currentSearchQuery: String? = null

    init {
        loadInitialPosts()
    }

    fun searchPosts(query: String?) {
        currentSearchQuery = query
        loadInitialPosts()
    }

    fun loadInitialPosts() {
        isLastPage = false
        lastVisiblePost = null
        _posts.value = emptyList()
        loadPosts()
    }

    fun loadNextPage() {
        if (_isLoading.value == true || isLastPage) return
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val (newPosts, newLastVisible) = repository.getPosts(10, lastVisiblePost, currentSearchQuery)

                // LOG "MATA-MATA" PENTING #3
                Log.d("HomeViewModel", "Menerima ${newPosts.size} postingan baru dari repository.")

                if (newPosts.isNotEmpty()) {
                    val currentList = _posts.value ?: emptyList()
                    _posts.value = currentList + newPosts
                    lastVisiblePost = newLastVisible
                } else {
                    isLastPage = true
                }
            } catch (e: Exception) {
                // LOG "MATA-MATA" PENTING #4: Jika ada error lain di dalam ViewModel
                Log.e("HomeViewModel", "Error di ViewModel saat loadPosts: ", e)
                _posts.value = emptyList() // Pastikan daftar tetap kosong saat error
            } finally {
                _isLoading.value = false
            }
        }
    }
}