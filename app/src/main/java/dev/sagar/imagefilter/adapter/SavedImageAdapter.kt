package dev.sagar.imagefilter.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.sagar.imagefilter.databinding.ItemContsinerSavedImageBinding
import dev.sagar.imagefilter.listeneres.SavedImageListeners
import java.io.File

class SavedImageAdapter(private val savedImages : List<Pair<File, Bitmap>>,
    private val savedImageListeners: SavedImageListeners) :
    RecyclerView.Adapter<SavedImageAdapter.SavedImageViewHolder>(){

    inner class SavedImageViewHolder( val binding : ItemContsinerSavedImageBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImageViewHolder {
        val binding = ItemContsinerSavedImageBinding.inflate(LayoutInflater.from(parent.context),
            parent,false
        )
        return SavedImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImageViewHolder, position: Int) {
        with(holder){
            with(savedImages[position]){
                binding.imageSavedImage.setImageBitmap(second)
                binding.imageSavedImage.setOnClickListener {
                    savedImageListeners.onImageClicked(first)
                }
            }
        }
    }

    override fun getItemCount()= savedImages.size
}