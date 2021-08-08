package uz.texnopos.instagram.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.instagram.data.Resource
import uz.texnopos.instagram.data.helper.PostHelper
import uz.texnopos.instagram.data.model.Post

class HomeViewModel(private val postHelper: PostHelper): ViewModel() {

    private var mutablePost: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val posts: LiveData<Resource<List<Post>>> get() = mutablePost

    fun getCurrentUserPosts(){
        mutablePost.value = Resource.loading()
        postHelper.getAllPosts(
            {
                mutablePost.value = Resource.success(it)
            },
            {
                mutablePost.value = Resource.error(it)
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

    private var mutableUserLikedPost: MutableLiveData<Resource<String>> = MutableLiveData()
    val removeUserLikedPost: LiveData<Resource<String>> get() = mutableUserLikedPost

    fun removeUserLikedPost(post: Post){
        mutableUserLikedPost.value = Resource.loading()
        postHelper.removeUserLikedPosts(post,
            {
                mutableUserLikedPost.value = Resource.success("success")
            },
            {
                mutableUserLikedPost.value = Resource.error(it)
            }
        )
    }
}