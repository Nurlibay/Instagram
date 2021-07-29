package uz.texnopos.instagram.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.texnopos.instagram.data.Resource
import uz.texnopos.instagram.data.helper.PostHelper
import uz.texnopos.instagram.data.model.Post

class PostsViewModel(private val postHelper: PostHelper): ViewModel() {

    private var mutablePost: MutableLiveData<Resource<List<Post>>> = MutableLiveData()
    val posts: LiveData<Resource<List<Post>>> get() = mutablePost

    fun getCurrentUserPosts(){
        mutablePost.value = Resource.loading()
        postHelper.getCurrentUserAllPosts(
            {
                mutablePost.value = Resource.success(it)
            },
            {
                mutablePost.value = Resource.error(it)
            }
        )
    }
}