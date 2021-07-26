package uz.texnopos.instagram.data.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

}