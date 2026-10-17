package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryFileImpl

class PostViewModel(application: Application) : AndroidViewModel(application) {


    private val repository: PostRepository = PostRepositoryFileImpl(application.applicationContext)

    val data: LiveData<List<Post>> = repository.getAll()

    private val _edited = MutableLiveData<Post>()
    val edited: LiveData<Post> = _edited

    init {
        _edited.value = Post(0, "", "", "", 0, false, 0, 0, null, null)
    }

    fun save(content: String) {
        _edited.value?.let { currentPost ->
            val text = content.trim()
            if (currentPost.content != text) {
                repository.save(currentPost.copy(content = text))
            }
        }
        _edited.value = Post(0, "", "", "", 0, false, 0, 0, null, null)
    }

    fun edit(post: Post) {
        _edited.value = post
    }

    fun createNew() {
        _edited.value = Post(0, "", "", "", 0, false, 0, 0, null, null)
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun shareById(id: Long) = repository.shareById(id)
    fun removeById(id: Long) = repository.removeById(id)
}