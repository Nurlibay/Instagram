package uz.texnopos.instagram.data.model

data class User(
    val uid: String = "",
    val email: String = "",
    var name: String = "",
    var username: String = "",
    var biography: String = "",
    var image: String = "",
    var postCount: Int = 0,
    var followersCount: Int = 0,
    var followingsCount: Int = 0
)
