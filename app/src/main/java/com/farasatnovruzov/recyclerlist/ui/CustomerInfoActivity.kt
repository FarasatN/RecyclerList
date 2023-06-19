package com.farasatnovruzov.recyclerlist.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.farasatnovruzov.recyclerlist.BaseActivity
import com.farasatnovruzov.recyclerlist.R
import com.farasatnovruzov.recyclerlist.databinding.ActivityCustomerInfoBinding


class CustomerInfoActivity : BaseActivity() {
    lateinit var binding: ActivityCustomerInfoBinding
//    private var mCameraUri: Uri? = null
//    private var mGalleryUri: Uri? = null
//    private var mProfileUri: Uri? = null
//
//    private val profileLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                val uri = it.data?.data!!
//                mProfileUri = uri
//                binding.customerInfoImg.setLocalImage(uri, true)
//            } else {
//                parseError(it)
//            }
//        }
//    private val galleryLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                if (it.data?.hasExtra(ImagePicker.EXTRA_FILE_PATH)!!) {
//                    val uri = it.data?.data!!
//                    mGalleryUri = uri
//                    binding.customerInfoImg.setLocalImage(uri)
//                } else if (it.data?.hasExtra(ImagePicker.MULTIPLE_FILES_PATH)!!) {
//                    val files = ImagePicker.getAllFile(it.data) as ArrayList<Uri>
//                    if (files.size > 0) {
//                        val uri = files[0] // first image
//                        mGalleryUri = uri
//                        binding.customerInfoImg.setLocalImage(uri)
//                    }
//                } else {
//                    parseError(it)
//                }
//            } else {
//                parseError(it)
//            }
//        }
//
//    private val cameraLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                val uri = it.data?.data!!
//                mCameraUri = uri
//                binding.customerInfoImg.setLocalImage(uri, false)
//            } else {
//                parseError(it)
//            }
//        }
//
//    private fun parseError(activityResult: ActivityResult) {
//        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(activityResult.data), Toast.LENGTH_SHORT)
//                .show()
//        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//    fun pickGalleryImage(view: View) {
//        galleryLauncher.launch(
//            ImagePicker.with(this)
//                .crop()
//                .galleryOnly()
//                .setMultipleAllowed(true)
////                .setOutputFormat(Bitmap.CompressFormat.WEBP)
//                .cropFreeStyle()
//                .galleryMimeTypes( // no gif images at all
//                    mimeTypes = arrayOf(
//                        "image/png",
//                        "image/jpg",
//                        "image/jpeg"
//                    )
//                )
//                .createIntent()
//        )
//    }
//    fun pickCameraImage() {
//        cameraLauncher.launch(
//            ImagePicker.with(this)
//                .crop()
//                .cameraOnly()
//                .maxResultSize(1080, 1920, true)
//                .createIntent()
//        )
//    }
////    fun showImage(view: View) {
////        val uri = when (view) {
////            imgProfile -> mProfileUri
////            imgCamera -> mCameraUri
////            imgGallery -> mGalleryUri
////            else -> null
////        }
////
////        uri?.let {
////            startActivity(IntentUtils.getUriViewIntent(this, uri))
////        }
////    }
//
//    fun pickProfileImage() {
//        ImagePicker.with(this)
//            .crop()
//            .cropOval()
//            .maxResultSize(512, 512, true)
//            .provider(ImageProvider.BOTH) // Or bothCameraGallery()
//            .setDismissListener {
//                Log.d("ImagePicker", "onDismiss")
//            }
//            .createIntentFromDialog { profileLauncher.launch(it) }
//    }


//---------------------------------------------------------




    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_IMAGE_PICK = 2


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    // Do something with the imageBitmap (e.g., display it in an ImageView)
                    binding.customerInfoImg.setImageBitmap(imageBitmap)
                }
                REQUEST_IMAGE_PICK -> {
                    val imageUri = data?.data
                    // Do something with the imageUri (e.g., load it into an ImageView using a library like Glide or Picasso)
                    binding.customerInfoImg.setImageURI(imageUri)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Registers a photo picker activity launcher in single-select mode.
        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                binding.customerInfoImg.setImageURI(uri)
//                Glide.with(this)
//                    .load(uri)
//                    .into(binding.customerInfoImg)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }

        binding.customerInfoImg.setOnClickListener {


            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
//                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            val mimeType = "image/*"
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.SingleMimeType(mimeType)))

            }else{
                val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery",
//                "Cancel"
                )
                val builder = AlertDialog.Builder(this)
    //            builder.setTitle("Select Image")
                builder.setItems(options) { dialog, item ->
                    when {
                        options[item] == "Take Photo" -> {
                            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                        }
                        options[item] == "Choose from Gallery" -> {
                            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            startActivityForResult(intent, REQUEST_IMAGE_PICK)
                        }
    //                    options[item] == "Cancel" -> {
    //                        dialog.dismiss()
    //                    }
                        else -> dialog.dismiss()
                    }
                }
                builder.show()
            }
        }




        binding.clientTypeCard.setOnClickListener {
            val intent = Intent(this, ErrorActivity::class.java)
            startActivity(intent)
        }
        binding.phoneNumberCard.setOnClickListener {
            val intent = Intent(this, InformationActivity::class.java)
            startActivity(intent)
        }
        binding.emailCard.setOnClickListener {
            val intent = Intent(this, EditPhoneNumberActivity::class.java)
            startActivity(intent)
        }
        binding.keywordCard.setOnClickListener {
            val intent = Intent(this, EditEmailActivity::class.java)
            startActivity(intent)
        }
    }
}