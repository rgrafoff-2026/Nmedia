package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityNewPostBinding
import ru.netology.nmedia.util.AndroidUtils

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val initialText = intent.getStringExtra(Intent.EXTRA_TEXT) ?: ""
        binding.edit.setText(initialText)
        binding.edit.setSelection(initialText.length) // Курсор в конец

        AndroidUtils.showKeyboard(binding.edit)

        binding.ok.setOnClickListener {
            val content = binding.edit.text.toString()
            if (content.isBlank()) {
                setResult(Activity.RESULT_CANCELED)
            } else {
                val resultIntent = Intent().apply {
                    putExtra(Intent.EXTRA_TEXT, content)
                }
                setResult(Activity.RESULT_OK, resultIntent)
            }
            finish()
        }
    }
}