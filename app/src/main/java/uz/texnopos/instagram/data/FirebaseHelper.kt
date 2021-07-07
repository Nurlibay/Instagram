package uz.texnopos.instagram.data

import com.google.firebase.auth.FirebaseAuth

class FirebaseHelper(private val auth: FirebaseAuth) {

    fun signUp(email: String,
               password: String,
               onSuccess: () -> Unit,
               onFailure: (msg: String?) -> Unit)
    {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailure.invoke(it.localizedMessage)
            }
    }
}