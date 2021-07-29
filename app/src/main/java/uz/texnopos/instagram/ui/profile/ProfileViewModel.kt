package uz.texnopos.instagram.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.instagram.data.Resource
import uz.texnopos.instagram.data.helper.ProfileHelper
import uz.texnopos.instagram.data.model.Post
import uz.texnopos.instagram.data.model.User

class ProfileViewModel(private val profileHelper: ProfileHelper, private val postHelper: ProfileHelper): ViewModel() {
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

    private var mutablePosts: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val posts: LiveData<Resource<List<Post>>> get() = mutablePosts

    fun getCurrentUserPosts(){
        mutablePosts.value = Resource.loading()
        postHelper.getCurrentUserPosts(
            {
                mutablePosts.value = Resource.success(it)
            },
            {
                mutablePosts.value = Resource.error(it)
            }
        )
    }
}