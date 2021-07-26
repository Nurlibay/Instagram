package uz.texnopos.instagram.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uz.texnopos.instagram.data.Resource
import uz.texnopos.instagram.data.helper.PostHelper
import androidx.lifecycle.ViewModel as ViewModel

class AddPostViewModel(private val postHelper: PostHelper) : ViewModel() {

    private var mutablePost: MutableLiveData<Resource<String>> = MutableLiveData()
    val post: LiveData<Resource<String>> get() = mutablePost

    fun sendNewPost(byteArray: ByteArray, description: String) {
        mutablePost.value = Resource.loading()
        postHelper.sendNewPost(byteArray, description,
            {
                mutablePost.value = Resource.success("success")
            },
            {
                mutablePost.value = Resource.error(it)
            })
    }
}