package uz.texnopos.instagram.data.helper

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import uz.texnopos.instagram.data.Const
import uz.texnopos.instagram.data.model.User
import java.util.*

class EditProfileHelper(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) {
    fun sendNewUserImage(
        byteArray: ByteArray,
        onSuccess: () -> Unit, onFailure: (msg: String?) -> Unit
    ) {
        val compressedPostRef = storage.reference.child("userImages/${UUID.randomUUID()}")
        val uploadTask = compressedPostRef.putBytes(byteArray)
        uploadTask.addOnSuccessListener {

            compressedPostRef.downloadUrl.addOnSuccessListener {

                //val user = User(uid = auth.currentUser!!.uid, image = it.toString())

                //db.collection(Const.USERS).document(auth.currentUser!!.uid).update(image = it.toString())

                db.document("${Const.USERS}/${auth.currentUser!!.uid}").update("image", it.toString())
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