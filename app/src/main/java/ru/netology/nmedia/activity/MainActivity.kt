package ru.netology.nmedia.activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.OnInteractionListener
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val viewModel: PostViewModel by viewModels()

        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onEdit(post: Post) = viewModel.edit(post)
            override fun onLike(post: Post) = viewModel.likeById(post.id)
            override fun onRemove(post: Post) = viewModel.removeById(post.id)
            override fun onShare(post: Post) {
                val intent = Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_TEXT, post.content)
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(intent, getString(R.string.chooser_share_post)))
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts -> adapter.submitList(posts) }

        viewModel.edited.observe(this) { post ->
            if (post.id != 0L) {
                binding.editModeGroup.visibility = View.VISIBLE
                with(binding.content) { setText(post.content); AndroidUtils.showKeyboard(this) }
            } else {
                binding.editModeGroup.visibility = View.GONE
                with(binding.content) { setText(""); clearFocus(); AndroidUtils.hideKeyboard(this) }
            }
        }

        binding.cancelEdit.setOnClickListener { viewModel.cancelEdit() }

        binding.save.setOnClickListener {
            with(binding.content) {
                if (text.isNullOrBlank()) return@setOnClickListener
                viewModel.save(text.toString())
                setText(""); clearFocus(); AndroidUtils.hideKeyboard(this)
            }
        }

        val newPostLauncher = registerForActivityResult(NewPostResultContract) { result ->
            result?.let { viewModel.save(it) }
        }
        binding.fab.setOnClickListener { newPostLauncher.launch() }
    }
}