package uz.texnopos.instagram.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.texnopos.instagram.data.model.Post
import uz.texnopos.instagram.databinding.ItemPostBinding
import java.text.SimpleDateFormat

class PostAdapter : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var onDoubleClicked: (post: Post) -> Unit = {}
    fun setOnDoubleClickListener(onDoubleClicked: (post: Post) -> Unit) {
        this.onDoubleClicked = onDoubleClicked
    }

    private var onLiked: (postId: String) -> Unit = { }
    fun setOnLikeListener(onLiked: (postId: String) -> Unit) {
        this.onLiked = onLiked
    }

    inner class PostViewHolder(private val binding: ItemPostBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(post: Post){
            binding.tvDescription.text = post.description
            binding.tvLikesCount.text = post.likes.toString()
            Glide
                .with(binding.root.context)
                .load(post.imageUrl)
                .centerCrop()
                .into(binding.postImage)

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
            val dateString = simpleDateFormat.format(post.createdDate)
            binding.tvCreatedDate.text = String.format("Date: %s", dateString)
            var doubleClick = false
            binding.postImage.setOnClickListener {
                if (doubleClick) {
                    onDoubleClicked.invoke(post)
                    doubleClick = false
                } else {
                    doubleClick = true
                    GlobalScope.launch {
                        delay(500)
                        doubleClick = false
                    }
                }
            }
            binding.favoriteIcon.setOnClickListener {
                onLiked.invoke(post.id)
            }
        }
    }

    var models: List<Post> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemBinding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int {
        return models.size
    }


 }