package uz.texnopos.instagram.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.instagram.data.Resource
import uz.texnopos.instagram.data.helper.PostHelper
import uz.texnopos.instagram.data.model.Post

class LikedPostViewModel(private val postHelper: PostHelper): ViewModel() {

    private var mutableLikedPost: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val likedPost: LiveData<Resource<List<Post>>> get() = mutableLikedPost

    fun getUserLikedPosts(){
        mutableLikedPost.value = Resource.loading()
        postHelper.getUserLikedPosts(
            {
                mutableLikedPost.value = Resource.success(it)
            },
            {
                mutableLikedPost.value = Resource.error(it)
            }
        )
    }

    private var mutableLike: MutableLiveData<Resource<Post>> = MutableLiveData()
    val like: LiveData<Resource<Post>> get() = mutableLike
    fun onDoubleClicked(post: Post) {
        postHelper.onDoubleClicked(post,
            {
                mutableLike.value = Resource.success(it)
            },
            {
                mutableLike.value = Resource.error(it)
            })
    }

}