package uz.texnopos.instagram.ui.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import uz.texnopos.instagram.R
import uz.texnopos.instagram.data.model.Post
import uz.texnopos.instagram.databinding.ItemPostProfileBinding

class ProfilePostAdapter : RecyclerView.Adapter<ProfilePostAdapter.ProfilePostViewHolder>() {

    inner class ProfilePostViewHolder(private val binding: ItemPostProfileBinding): RecyclerView.ViewHolder(binding.root){
        fun populateModel(post: Post){
            Glide
                .with(binding.root.context)
                .load(post.imageUrl)
                .placeholder(R.drawable.avatar)
                .centerCrop()
                .into(binding.postImage)
        }
    }

    var models: List<Post> = listOf()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilePostViewHolder {
        val binding = ItemPostProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfilePostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfilePostViewHolder, position: Int) {
        holder.populateModel(models[position])
    }

    override fun getItemCount(): Int {
        return models.size
    }
}
