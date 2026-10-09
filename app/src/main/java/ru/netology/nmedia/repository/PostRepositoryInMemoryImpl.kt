// PostRepositoryInMemoryImpl.kt
package ru.netology.nmedia.repository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemoryImpl : PostRepository {
    private var nextId = 10L
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу...",
            published = "21 мая в 18:36",
            likes = 10,
            likedByMe = false,
            shares = 5,   // 👈 ДОБАВЛЕНО
            views = 150   // 👈 ДОБАВЛЕНО
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаний хватит на всех: на следующей неделе разбираемся с разработкой мобильных приложений...",
            published = "18 сентября в 10:12",
            likes = 20,
            likedByMe = false,
            shares = 2,   // 👈 ДОБАВЛЕНО
            views = 300   // 👈 ДОБАВЛЕНО
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Ура у меня ДР!!! Пойду попишу много новых кодов! Чем еще занятся в свой ДР!!!",
            published = "04 апреля в 07:07",
            likes = 1499,
            likedByMe = false,
            shares = 7,   // 👈 ДОБАВЛЕНО
            views = 999   // 👈 ДОБАВЛЕНО
        )
    )
    private val data = MutableLiveData(posts)

    override fun getAll(): LiveData<List<Post>> = data

    override fun save(post: Post) {
        if (post.id == 0L) {
            posts = listOf(post.copy(id = nextId++, author = "Me", published = "now")) + posts
        } else {
            posts = posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        }
        data.value = posts
    }

    override fun likeById(id: Long) {
        posts = posts.map { if (it.id != id) it else it.copy(likedByMe = !it.likedByMe, likes = if (it.likedByMe) it.likes - 1 else it.likes + 1) }
        data.value = posts
    }

    override fun shareById(id: Long) {
        posts = posts.map { if (it.id != id) it else it.copy(shares = it.shares + 1) }
        data.value = posts
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }
}