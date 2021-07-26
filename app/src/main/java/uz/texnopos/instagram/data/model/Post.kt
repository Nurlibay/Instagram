package uz.texnopos.instagram.data.model

data class Post(
    val id: String,
    val userId: String,
    val imageUrl: String,
    val description: String,
    val createdDate: Long = System.currentTimeMillis(),
    var likes: Long = 0,
    var views: Long = 0
)