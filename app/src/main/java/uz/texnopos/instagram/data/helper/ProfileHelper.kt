package uz.texnopos.instagram.data.helper

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import uz.texnopos.instagram.data.Const
import uz.texnopos.instagram.data.model.Post
import uz.texnopos.instagram.data.model.User

class ProfileHelper(private val auth: FirebaseAuth, private val db: FirebaseFirestore) {

    fun getProfileData(onSuccess: (user: User) -> Unit, onFailure: (msg: String?) -> Unit) {
        db.collection(Const.USERS).document(auth.currentUser!!.uid).get()
            .addOnSuccessListener {
                val result = it.toObject(User::class.java)
                result?.let { user ->
                    onSuccess.invoke(user)
                } ?: onFailure.invoke("User data is empty !")
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun editProfile(user: User, onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit){

        // db.collection(Const.USERS).document(user.uid).set(user)

        db.document("${Const.USERS}/${user.uid}").set(user)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }

    fun getCurrentUserPosts(onSuccess: (posts: List<Post>) -> Unit, onFailure: (msg: String?) -> Unit){
        db.collection(Const.POSTS).whereEqualTo("userId", auth.currentUser!!.uid)
            //.orderBy("createdDate", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                val res = it.documents.map { doc ->
                    doc.toObject(Post::class.java)!!
                }
                onSuccess.invoke(res)
                Log.d("natiyje", res.toString())
            }
            .addOnFailureListener {
                onFailure.invoke(it.message)
            }
    }
}