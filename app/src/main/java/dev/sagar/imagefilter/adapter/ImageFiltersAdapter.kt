package dev.sagar.imagefilter.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import dev.sagar.imagefilter.R
import dev.sagar.imagefilter.data.ImageFilter
import dev.sagar.imagefilter.databinding.ItemContainerFilterBinding
import dev.sagar.imagefilter.listeneres.ImageFilterListener

class ImageFiltersAdapter(private val imageFilters : List<ImageFilter>
    , private val imageFilterListener: ImageFilterListener) :
    RecyclerView.Adapter<ImageFiltersAdapter.ImageFilterViewHolder>(){

    private var selectedFilteredPosition = 0
    private var previouslySelectedPosition = 0

    inner class ImageFilterViewHolder(val binding: ItemContainerFilterBinding) :
            RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageFilterViewHolder {
        val binding = ItemContainerFilterBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return ImageFilterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageFilterViewHolder, @SuppressLint("RecyclerView") position: Int) {
        with(holder){
            with(imageFilters[position]){
                binding.imageFilterPreview.setImageBitmap(filterPreview)
                binding.TextFilterName.text = name
                binding.root.setOnClickListener{
                    if (position != selectedFilteredPosition){
                        imageFilterListener.onFilterSelected(this)
                        previouslySelectedPosition = selectedFilteredPosition
                        selectedFilteredPosition = position
                        with(this@ImageFiltersAdapter){
                            notifyItemChanged(previouslySelectedPosition, Unit)
                            notifyItemChanged(selectedFilteredPosition, Unit)
                        }
                    }
                }
            }
            binding.TextFilterName.setTextColor(
                ContextCompat.getColor(
                    binding.TextFilterName.context,
                    if (selectedFilteredPosition == position)
                        R.color.PrimaryDark
                    else
                        R.color.PrimaryText
                )
            )
        }
    }

    override fun getItemCount() = imageFilters.size
}