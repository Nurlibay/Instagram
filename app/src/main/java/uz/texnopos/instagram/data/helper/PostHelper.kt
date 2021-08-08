package uz.texnopos.instagram.data.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import uz.texnopos.instagram.data.Const
import uz.texnopos.instagram.data.model.Post
import java.util.*

class PostHelper(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) {

    fun sendNewPost(
        byteArray: ByteArray, description: String,
        onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit
    ) {
        val compressedPostRef = storage.reference.child("compressedPosts/${UUID.randomUUID()}")
        val uploadTask = compressedPostRef.putBytes(byteArray)
        uploadTask.addOnSuccessListener {

            compressedPostRef.downloadUrl.addOnSuccessListener {

                // create a post
                val post = Post(
                    id = UUID.randomUUID().toString(),
                    imageUrl = it.toString(),
                    userId = auth.currentUser!!.uid, description = description
                )

                db.document("${Const.POSTS}/${post.id}").set(post)
                    .addOnSuccessListener {
                        onSuccess.invoke()
                    }
                    .addOnFailureListener {
                        onFailure.invoke(it.localizedMessage)
                    }

            }

        }.addOnFailureListener {
            onFailure.invoke(it.localizedMessage)
        }
    }

    fun getCurrentUserAllPosts(onSuccess: (posts: List<Post>) -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection(Const.POSTS).whereEqualTo("userId", auth.currentUser!!.uid)
            .orderBy("createdDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val res = it.documents.map { doc ->
                    doc.toObject(Post::class.java)!!
                }
                onSuccess.invoke(res)
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun getAllPosts(onSuccess: (posts: List<Post>) -> Unit, onFailure: (msg: String?) -> Unit) {
        db.collection(Const.POSTS).orderBy("createdDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val res = it.documents.map { doc ->
                    doc.toObject(Post::class.java)!!
                }
                onSuccess.invoke(res)
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun onDoubleClicked(post: Post, onSuccess: (post: Post) -> Unit,
    onFailure: (msg: String?) -> Unit) {
        db.document("${Const.USERS}/${auth.currentUser!!.uid}/${Const.LIKED_POSTS}/${post.id}")
            .get()
            .addOnSuccessListener {
                if (!it.exists()) {
                    addPostToLikedPosts(post, onSuccess, onFailure)
                }
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }
    }

    private fun addPostToLikedPosts(post: Post, onSuccess: (post: Post) -> Unit,
                                    onFailure: (msg: String?) -> Unit) {
        db.collection(Const.USERS).document(auth.currentUser!!.uid).collection(Const.LIKED_POSTS).document(post.id).set(post)
            .addOnSuccessListener {
                getPostLikesCount(post, onSuccess, onFailure)
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }
    }

    private fun getPostLikesCount(post: Post, onSuccess: (post: Post) -> Unit,
                                  onFailure: (msg: String?) -> Unit) {
        db.collection(Const.POSTS).document(post.id).get()
            .addOnSuccessListener {
                val serverPost = it.toObject(Post::class.java)!!
                increaseLikeCount(post, serverPost.likedUsers, onSuccess, onFailure)
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }
    }

    private fun increaseLikeCount(post: Post, likedUsers: MutableList<String>, onSuccess: (post: Post) -> Unit,
                                  onFailure: (msg: String?) -> Unit) {
        likedUsers.add(auth.currentUser!!.uid)
        db.collection(Const.POSTS).document(post.id)
            .update("likedUsers", likedUsers)
            .addOnSuccessListener {
                post.likedUsers = likedUsers
                onSuccess.invoke(post)
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }
    }

    fun getUserLikedPosts(onSuccess: (posts: List<Post>) -> Unit, onFailure: (msg: String?) -> Unit) {
        db.collection(Const.USERS).document(auth.currentUser!!.uid).collection(Const.LIKED_POSTS).get()
            .addOnSuccessListener {
                val res = it.documents.map { doc ->
                    doc.toObject(Post::class.java)!!
                }
                onSuccess.invoke(res)
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }
    }

    fun removeUserLikedPosts(post: Post, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection(Const.POSTS).get()
            .addOnSuccessListener {
                val res = it.documents.map { doc->
                    doc.toObject(Post::class.java)
                }
                res.forEach { serverPost ->
                    serverPost!!.likedUsers.forEach {
                        if (it.contains(post.userId)){
                            serverPost.likedUsers.remove(it)
                        }
                    }
                }
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }
    }
}