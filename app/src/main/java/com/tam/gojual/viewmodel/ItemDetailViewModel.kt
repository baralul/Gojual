package com.tam.gojual.viewmodel // Sesuaikan package

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tam.gojual.data.model.Post
import com.tam.gojual.data.repository.PostRepository
import kotlinx.coroutines.launch

class ItemDetailViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _post = MutableLiveData<Post?>()
    val post: LiveData<Post?> = _post

    fun fetchPostDetails(postId: String) {
        viewModelScope.launch {
            val postDetails = repository.getPostById(postId)
            _post.value = postDetails
        }
    }
}