package com.farasatnovruzov.recyclerlist.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import com.farasatnovruzov.recyclerlist.BaseActivity
import com.farasatnovruzov.recyclerlist.R
import com.farasatnovruzov.recyclerlist.databinding.ActivityCustomerInfoBinding
import com.farasatnovruzov.recyclerlist.ui.util.FileUtil
import com.github.drjacky.imagepicker.ImagePicker
import com.github.drjacky.imagepicker.constant.ImageProvider
import com.github.drjacky.imagepicker.util.IntentUtils
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.launch


class CustomerInfoActivity : BaseActivity() {
    lateinit var binding: ActivityCustomerInfoBinding
    private var mCameraUri: Uri? = null
    private var mGalleryUri: Uri? = null
    private var mProfileUri: Uri? = null

    private val profileLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                mProfileUri = uri
                binding.customerInfoImg.setLocalImage(uri, true)
            } else {
                parseError(it)
            }
        }
    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data?.hasExtra(ImagePicker.EXTRA_FILE_PATH)!!) {
                    val uri = it.data?.data!!
                    mGalleryUri = uri
                    binding.customerInfoImg.setLocalImage(uri)
                } else if (it.data?.hasExtra(ImagePicker.MULTIPLE_FILES_PATH)!!) {
                    val files = ImagePicker.getAllFile(it.data) as ArrayList<Uri>
                    if (files.size > 0) {
                        val uri = files[0] // first image
                        mGalleryUri = uri
                        binding.customerInfoImg.setLocalImage(uri)
                    }
                } else {
                    parseError(it)
                }
            } else {
                parseError(it)
            }
        }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                mCameraUri = uri
                binding.customerInfoImg.setLocalImage(uri, false)
            } else {
                parseError(it)
            }
        }

    private fun parseError(activityResult: ActivityResult) {
        if (activityResult.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(activityResult.data), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


    fun pickGalleryImage(view: View) {
        galleryLauncher.launch(
            ImagePicker.with(this)
                .crop()
                .galleryOnly()
                .setMultipleAllowed(true)
//                .setOutputFormat(Bitmap.CompressFormat.WEBP)
                .cropFreeStyle()
                .galleryMimeTypes( // no gif images at all
                    mimeTypes = arrayOf(
                        "image/png",
                        "image/jpg",
                        "image/jpeg"
                    )
                )
                .createIntent()
        )
    }
    fun pickCameraImage() {
        cameraLauncher.launch(
            ImagePicker.with(this)
                .crop()
                .cameraOnly()
                .maxResultSize(1080, 1920, true)
                .createIntent()
        )
    }
//    fun showImage(view: View) {
//        val uri = when (view) {
//            imgProfile -> mProfileUri
//            imgCamera -> mCameraUri
//            imgGallery -> mGalleryUri
//            else -> null
//        }
//
//        uri?.let {
//            startActivity(IntentUtils.getUriViewIntent(this, uri))
//        }
//    }

    fun pickProfileImage() {
        ImagePicker.with(this)
            .crop()
            .cropOval()
            .maxResultSize(512, 512, true)
            .provider(ImageProvider.BOTH) // Or bothCameraGallery()
            .setDismissListener {
                Log.d("ImagePicker", "onDismiss")
            }
            .createIntentFromDialog { profileLauncher.launch(it) }
    }







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerInfoBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        imgProfile.setDrawableImage(R.drawable.ic_person, true)

        binding.customerInfoImg.setOnClickListener {
//            val intent = Intent(this, SuccessActivity::class.java)
//            startActivity(intent)


            pickProfileImage()
            pickCameraImage()
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