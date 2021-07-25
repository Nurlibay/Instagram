package uz.texnopos.instagram.ui.profile.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.instagram.data.Resource
import uz.texnopos.instagram.data.helper.ProfileHelper
import uz.texnopos.instagram.data.model.User

class EditProfileViewModel(private val profileHelper: ProfileHelper) : ViewModel() {

    private var mutableUser: MutableLiveData<Resource<User>> = MutableLiveData()
    val user: LiveData<Resource<User>> get() = mutableUser

    fun getCurrentUser(){
        mutableUser.value = Resource.loading()
        profileHelper.getProfileData(
            {
                mutableUser.value = Resource.success(it)
            },
            {
                mutableUser.value = Resource.error(it)
            }
        )
    }

    private val mutableProfileEdit: MutableLiveData<Resource<String>> = MutableLiveData()
    val profileEdit: LiveData<Resource<String>> get() = mutableProfileEdit

    fun editProfile(user: User) {
        mutableProfileEdit.value = Resource.loading()
        profileHelper.editProfile(user,
            {
                mutableProfileEdit.value = Resource.success("success")
            },
            {
                mutableProfileEdit.value = Resource.error(it)
            }
        )
    }
}