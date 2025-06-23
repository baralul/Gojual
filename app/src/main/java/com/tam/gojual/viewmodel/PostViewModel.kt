package com.tam.gojual.viewmodel // Sesuaikan package

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tam.gojual.data.model.Post
import com.tam.gojual.data.repository.PostRepository
import kotlinx.coroutines.launch

sealed class PostResult {
    object Success : PostResult()
    data class Error(val message: String) : PostResult()
    object Loading : PostResult()
}

class PostViewModel : ViewModel() {

    private val postRepository = PostRepository()

    private val _postResult = MutableLiveData<PostResult>()
    val postResult: LiveData<PostResult> = _postResult

    fun addPost(post: Post) {
        viewModelScope.launch {
            _postResult.value = PostResult.Loading
            val result = postRepository.addPost(post)
            result.onSuccess {
                _postResult.value = PostResult.Success
            }.onFailure {
                _postResult.value = PostResult.Error(it.message ?: "Gagal menambahkan post")
            }
        }
    }
}