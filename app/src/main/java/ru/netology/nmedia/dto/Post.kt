package ru.netology.nmedia.dto
data class Post(
    val id: Int,
    val author: String,
    val content: String,
    val published: String,
    var likes: Int,
    var likedByMe: Boolean,
    var shares: Int = 0,
    var views: Int = 0
)