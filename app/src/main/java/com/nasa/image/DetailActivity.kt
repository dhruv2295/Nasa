package com.nasa.image

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.nasa.image.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar2.setNavigationOnClickListener { finish() }

        val imageUrlHD = intent.getStringExtra("imageUrlHD")

        binding.toolbar2.title = intent.getStringExtra("title")
        binding.description.text = intent.getStringExtra("description")
        binding.description.movementMethod = ScrollingMovementMethod()


        Glide.with(this).load(intent.getStringExtra("imageUrl")).into(binding.actionImage)

        if (imageUrlHD != null && imageUrlHD != "") {
            Glide.with(this)
                .asBitmap()
                .load(imageUrlHD)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        binding.actionImage.setImageBitmap(resource)
                        binding.progressBar.visibility = View.GONE
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        binding.actionImage.setImageDrawable(placeholder)
                        binding.progressBar.visibility = View.GONE
                    }
                })
        }
    }
}