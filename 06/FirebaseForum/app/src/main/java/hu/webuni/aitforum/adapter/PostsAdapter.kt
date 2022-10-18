package hu.webuni.aitforum.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import hu.webuni.aitforum.CreatePostActivity
import hu.webuni.aitforum.data.Post
import hu.webuni.aitforum.databinding.PostRowBinding

class PostsAdapter : RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    lateinit var context: Context
    var  postsList = mutableListOf<Post>()
    var  postKeys = mutableListOf<String>()

    lateinit var currentUid: String

    constructor(context: Context, uid: String) : super() {
        this.context = context
        this.currentUid = uid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PostRowBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var post = postsList.get(holder.adapterPosition)

        holder.tvAuthor.text = post.author
        holder.tvTitle.text = post.title
        holder.tvBody.text = post.body

        if (currentUid == post.uid) {
            holder.btnDelete.visibility = View.VISIBLE
        } else {
            holder.btnDelete.visibility = View.GONE
        }

        holder.btnDelete.setOnClickListener {
            removePost(holder.adapterPosition)
        }

        if (post.imgUrl.isNotEmpty()) {
            holder.ivPhoto.visibility = View.VISIBLE
            Glide.with(context).load(post.imgUrl).into(holder.ivPhoto)
        } else {
            holder.ivPhoto.visibility = View.GONE
        }

    }

    fun addPost(post: Post, key: String) {
        postsList.add(post)
        postKeys.add(key)
        //notifyDataSetChanged()
        notifyItemInserted(postsList.lastIndex)
    }

    // when I remove the post object
    private fun removePost(index: Int) {
        FirebaseFirestore.getInstance().collection(
            CreatePostActivity.POSTS_COLLECTION).document(
            postKeys[index]
        ).delete()

        postsList.removeAt(index)
        postKeys.removeAt(index)
        notifyItemRemoved(index)
    }

    // when somebody else removes an object
    fun removePostByKey(key: String) {
        val index = postKeys.indexOf(key)
        if (index != -1) {
            postsList.removeAt(index)
            postKeys.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    inner class ViewHolder(val binding: PostRowBinding) : RecyclerView.ViewHolder(binding.root){
        var tvAuthor = binding.tvAuthor
        var tvTitle = binding.tvTitle
        var tvBody = binding.tvBody
        var btnDelete = binding.btnDelete
        var ivPhoto = binding.ivPhoto
    }
}