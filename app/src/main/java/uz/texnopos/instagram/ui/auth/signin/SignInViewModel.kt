package uz.texnopos.instagram.ui.auth.signin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.instagram.data.helper.AuthHelper
import uz.texnopos.instagram.data.Resource
import androidx.lifecycle.LiveData as LiveData

class SignInViewModel(private val authHelper: AuthHelper): ViewModel() {
    private var mutableSignInStatus: MutableLiveData<Resource<Any?>> = MutableLiveData()
    val signInStatus: LiveData<Resource<Any?>>
        get() = mutableSignInStatus

    fun signIn(email: String, password: String){
        mutableSignInStatus.value = Resource.loading()
        authHelper.signIn(email, password,
            {
                mutableSignInStatus.value = Resource.success(null)

            }, {
                mutableSignInStatus.value = Resource.error(it)
            }
        )
    }
}