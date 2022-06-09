package dev.sagar.imagefilter.activities.editimage

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.sagar.imagefilter.activities.filteredimage.FilteredImageActivity
import dev.sagar.imagefilter.activities.main.MainActivity
import dev.sagar.imagefilter.adapter.ImageFiltersAdapter
import dev.sagar.imagefilter.data.ImageFilter
import dev.sagar.imagefilter.databinding.ActivityEditImageBinding
import dev.sagar.imagefilter.listeneres.ImageFilterListener
import dev.sagar.imagefilter.utilities.displayToast
import dev.sagar.imagefilter.utilities.show
import dev.sagar.imagefilter.viewmodels.EditImageViewModel
import jp.co.cyberagent.android.gpuimage.GPUImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditImageActivity : AppCompatActivity() , ImageFilterListener{

    companion object{
        const val KEY_FILTERED_IMAGE_URI = "filteredImageUri"
    }

    private lateinit var binding: ActivityEditImageBinding
    private val viewModel : EditImageViewModel by viewModel()
    private lateinit var gpuImage : GPUImage

    // image bitmaps
    private lateinit var originalBitmap : Bitmap
    private val filteredBitmap: MutableLiveData<Bitmap>
    = MutableLiveData<Bitmap>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setListeners()
        setupObservers()
        prepareImagePreview()
    }

    private fun prepareImagePreview() {
        gpuImage = GPUImage(applicationContext)
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            viewModel.prepareImagePreview(imageUri)
        }
    }

    private fun setupObservers(){
        viewModel.imagePreviewUIState.observe( this, {
            val dataState = it?: return@observe
            binding.previewProgressBar.visibility = if (dataState.isLoading)    View.VISIBLE else View.GONE
            dataState.bitmap?.let { bitmap ->
                originalBitmap = bitmap
                filteredBitmap.value = bitmap
                with(originalBitmap){
                    gpuImage.setImage(this)
                    binding.imagePreview.show()
                    viewModel.loadImageFilters(bitmap)
                }
            } ?: kotlin.run {
                dataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })
        viewModel.imageFiltersUIState.observe(this, {
            val imageFiltersDataState = it?: return@observe
            binding.imageFiltersProgressBar.visibility =
                if (imageFiltersDataState.isLoading) View.VISIBLE else View.GONE
            imageFiltersDataState.imageFilters?.let { imageFilters ->
                ImageFiltersAdapter(imageFilters,this).also { adapter ->
                    binding.filtersRecyclerView.adapter = adapter
                }
            } ?: kotlin.run {
                imageFiltersDataState.error?.let { error ->
                    displayToast(error)
                }
            }
        })

        filteredBitmap.observe(this , { bitmap ->
            binding.imagePreview.setImageBitmap(bitmap)
        })

        viewModel.saveFilteredImageUiState.observe(this,{
            val saveFilteredImageDataState = it ?: return@observe
            if (saveFilteredImageDataState.isLoading){
                binding.imageSave.visibility = View.GONE
                binding.savingProgressBar.visibility = View.VISIBLE
            }
            else{
                binding.savingProgressBar.visibility = View.GONE
                binding.imageSave.visibility = View.VISIBLE
            }
            saveFilteredImageDataState.uri?.let {   savedImageUri ->
                displayToast("Image saved")
                Intent(
                    applicationContext,
                    FilteredImageActivity::class.java
                ).also {
                    it.putExtra(KEY_FILTERED_IMAGE_URI ,savedImageUri)
                    startActivity(it)
                }
            }?: kotlin.run {
                saveFilteredImageDataState.error?.let {
                    displayToast(it)
                }
            }
        })
    }

  /*  private fun displayImagePreview() {
        intent.getParcelableExtra<Uri>(MainActivity.KEY_IMAGE_URI)?.let { imageUri ->
            val inputStream = contentResolver.openInputStream(imageUri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            binding.imagePreview.setImageBitmap(bitmap)
            binding.imagePreview.visibility = View.VISIBLE
        }
    } */

    private fun setListeners() {
        binding.imageBack.setOnClickListener{
            onBackPressed()
        }

        binding.imageSave.setOnClickListener {
            filteredBitmap.value?.let { bitmap ->
                viewModel.saveFilteredImage(bitmap)
            }
        }

        // on long press to view original image ie to compare original image wit filterd image

        binding.imagePreview.setOnLongClickListener{
            binding.imagePreview.setImageBitmap(originalBitmap)
            return@setOnLongClickListener false
        }
        binding.imagePreview.setOnClickListener{
            binding.imagePreview.setImageBitmap(filteredBitmap.value)
        }
    }

    override fun onFilterSelected(imageFilter: ImageFilter) {
        with(imageFilter){
            with(gpuImage){
                setFilter(filter)
                filteredBitmap.value = bitmapWithFilterApplied
            }
        }
    }
}