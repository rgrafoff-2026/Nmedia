package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post
import java.io.File

class PostRepositoryFileImpl(private val context: Context) : PostRepository {
    private val gson = Gson()
    private val fileName = "posts.json"

    private val _data = MutableLiveData<List<Post>>()
    override fun getAll(): LiveData<List<Post>> = _data

    init {
        loadPosts()
    }

    private fun loadPosts() {
        val file = File(context.filesDir, fileName)

        val defaultPosts = listOf(
            Post(
                id = 1,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. А потом появились курсы по дизайну, разработке, аналитике и управлению.",
                published = "21 мая в 18:36",
                likes = 10,
                likedByMe = false,
                shares = 5,
                views = 150,
                video = "https://rutube.ru/video/6550a91e7e523f9503bed47e4c46d0cb",
                image = null
            ),
            Post(
                id = 2,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений. Ждём вас на занятиях!",
                published = "18 сентября в 10:12",
                likes = 20,
                likedByMe = false,
                shares = 2,
                views = 300,
                video = null,
                image = null
            ),
            Post(
                id = 3,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Кто не успел, тот опоздал: дедлайн подачи заявок на грант уже завтра.",
                published = "15 сентября в 12:00",
                likes = 5,
                likedByMe = false,
                shares = 1,
                views = 50,
                video = null,
                image = null
            )
        )

        if (!file.exists() || file.length() == 0L) {
            savePosts(defaultPosts)
        } else {
            val type = object : TypeToken<List<Post>>() {}.type
            val existingPosts: List<Post>? = gson.fromJson(file.readText(), type)

            // "Умное" обновление: добавляем исходные посты, если их удалили
            val existingIds = existingPosts?.map { it.id } ?: emptyList()
            val postsToAdd = defaultPosts.filter { it.id !in existingIds }

            val finalPosts = (existingPosts ?: emptyList()) + postsToAdd

            if (postsToAdd.isNotEmpty()) {
                savePosts(finalPosts)
            } else {
                _data.value = finalPosts
            }
        }
    }

    private fun savePosts(posts: List<Post>) {
        val file = File(context.filesDir, fileName)
        file.writeText(gson.toJson(posts))
        _data.value = posts
    }

    override fun save(post: Post) {
        val currentPosts = _data.value ?: emptyList()
        val newPosts = if (post.id == 0L) {
            val newId = (currentPosts.maxOfOrNull { it.id } ?: 0L) + 1
            currentPosts + post.copy(id = newId, author = "Я", published = "только что")
        } else {
            currentPosts.map { if (it.id == post.id) post else it }
        }
        savePosts(newPosts)
    }

    override fun likeById(id: Long) {
        val currentPosts = _data.value ?: return
        val newPosts = currentPosts.map {
            if (it.id == id) it.copy(likedByMe = !it.likedByMe, likes = if (it.likedByMe) it.likes - 1 else it.likes + 1) else it
        }
        savePosts(newPosts)
    }

    override fun shareById(id: Long) {
        val currentPosts = _data.value ?: return
        val newPosts = currentPosts.map { if (it.id == id) it.copy(shares = it.shares + 1) else it }
        savePosts(newPosts)
    }

    override fun removeById(id: Long) {
        val currentPosts = _data.value ?: return
        val newPosts = currentPosts.filter { it.id != id }
        savePosts(newPosts)
    }
}