package uz.texnopos.instagram.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.instagram.data.Resource
import uz.texnopos.instagram.data.helper.ProfileHelper
import uz.texnopos.instagram.data.model.User

class ProfileViewModel(private val profileHelper: ProfileHelper): ViewModel() {
    private var mutableProfile: MutableLiveData<Resource<User>> = MutableLiveData()
    val profile: LiveData<Resource<User>>
    get() = mutableProfile

    fun getProfileData(){
        mutableProfile.value = Resource.loading()
        profileHelper.getProfileData(
            {
                mutableProfile.value = Resource.success(it)
            },
            {
                mutableProfile.value = Resource.error(it)
            }
        )
    }
}