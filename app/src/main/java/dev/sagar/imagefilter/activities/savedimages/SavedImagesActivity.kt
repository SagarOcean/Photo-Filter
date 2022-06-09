package dev.sagar.imagefilter.activities.savedimages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.FileProvider
import dev.sagar.imagefilter.R
import dev.sagar.imagefilter.activities.editimage.EditImageActivity
import dev.sagar.imagefilter.activities.filteredimage.FilteredImageActivity
import dev.sagar.imagefilter.adapter.SavedImageAdapter
import dev.sagar.imagefilter.databinding.ActivitySavedImagesBinding
import dev.sagar.imagefilter.listeneres.SavedImageListeners
import dev.sagar.imagefilter.utilities.displayToast
import dev.sagar.imagefilter.viewmodels.SavedImagesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class SavedImagesActivity : AppCompatActivity() , SavedImageListeners {

    private lateinit var binding: ActivitySavedImagesBinding
    private val viewModel : SavedImagesViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySavedImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpObserver()
        setListeners()
        viewModel.loadSavedImages()
    }

    private fun setListeners() {
        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setUpObserver() {
        viewModel.savedImagesUiState.observe(this,{
            val savedImagesDataState = it?: return@observe
            binding.savedImagesProgressBar.visibility =
                if (savedImagesDataState.isLoading) View.VISIBLE else View.GONE
            savedImagesDataState.savedImages?.let {
                SavedImageAdapter(it,this).also { adapter ->
                    with(binding.savedImagesRecyclerView){
                        this.adapter = adapter
                        visibility = View.VISIBLE
                    }
                }
            }?: run{
                savedImagesDataState.error?.let {
                    displayToast(it)
                }
            }
        })
    }

    override fun onImageClicked(file: File) {
        val fileUri = FileProvider.getUriForFile(
            applicationContext,
            "${packageName}.provider",
            file
        )
        Intent(
            applicationContext,
            FilteredImageActivity::class.java
        ).also { filterImageIntent ->
            filterImageIntent.putExtra(EditImageActivity.KEY_FILTERED_IMAGE_URI, fileUri)
            startActivity(filterImageIntent)
        }
    }
}