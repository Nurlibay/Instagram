package uz.texnopos.instagram.ui.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.instagram.data.helper.AuthHelper
import uz.texnopos.instagram.data.Resource

class SignupViewModel(private val authHelper: AuthHelper) : ViewModel() {
    private var mutableSignUpStatus: MutableLiveData<Resource<String?>> = MutableLiveData()
    val signUpStatus: LiveData<Resource<String?>>
        get() = mutableSignUpStatus

    fun signUp(email: String, password: String) {
        mutableSignUpStatus.value = Resource.loading()
        authHelper.signUp(email, password,
            {
                addUserToDb()
            },
            {
                mutableSignUpStatus.value = Resource.error(it)
            }
        )
    }

    private fun addUserToDb(){
        authHelper.addUserToDb(
            {
                mutableSignUpStatus.value = Resource.success(null)
            },
            {
                mutableSignUpStatus.value = Resource.error(it)
            }
        )
    }
}