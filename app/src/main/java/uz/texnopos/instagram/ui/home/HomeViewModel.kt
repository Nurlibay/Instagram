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

    private var mutableLike: MutableLiveData<Resource<Int>> = MutableLiveData()
    val like: LiveData<Resource<Int>> get() = mutableLike
    fun onDoubleClicked(post: Post) {
        mutableLike.value = Resource.loading()
        postHelper.onDoubleClicked(post,
            {
                mutableLike.value = Resource.success(it)
            },
            {
                mutableLike.value = Resource.error(it)
            })
    }
}