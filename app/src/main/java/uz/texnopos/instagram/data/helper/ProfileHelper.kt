package uz.texnopos.instagram.data.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import uz.texnopos.instagram.data.Const
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
}