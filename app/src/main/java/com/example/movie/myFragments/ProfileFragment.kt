package com.example.movie.myFragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.movie.view.LoginActivity
import com.example.movie.R
import com.example.movie.api.RequestConstants
import com.example.movie.model.Singleton
import com.example.movie.view.MapsActivity
import com.example.movie.view_model.ProfileViewModel
import com.example.movie.view_model.ViewModelProviderFactory
import java.io.File
import java.io.IOException

class ProfileFragment : Fragment() {
    private lateinit var preferences: SharedPreferences
    private lateinit var nameInfo: TextView
    private lateinit var emailInfo: TextView
    private lateinit var logout: Button
    private lateinit var map: Button
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var progressBar: ProgressBar
    private lateinit var profileListViewModel: ProfileViewModel
    private lateinit var changeAvatarPhoto: Button
    private lateinit var avatarIm: ImageView
    private var photoPath: String? = null
    private var selectedPhotoFile: File? = null
    val REQUEST_TAKE_PHOTO=1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val viewModelProviderFactory = ViewModelProviderFactory(context = this.activity as Context)
        profileListViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(ProfileViewModel::class.java)
        val rootView = inflater.inflate(R.layout.activity_profile, container, false) as ViewGroup
        profileListViewModel.liveData.observe(this, Observer {
            when (it) {
                is ProfileViewModel.State.ShowLoading -> {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
                is ProfileViewModel.State.HideLoading -> {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
                is ProfileViewModel.State.Result -> {
                    logout()
                }
                is ProfileViewModel.State.BadResult -> {
                    Toast.makeText(context, "Something is wrong", Toast.LENGTH_LONG).show()
                }
            }
        })
        bindView(rootView)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        logout.setOnClickListener {
            profileListViewModel.deleteProfileSession()
        }

        changeAvatarPhoto.setOnClickListener {
            getPermissions()

        map.setOnClickListener {
            val intent = Intent(context, MapsActivity::class.java)
            startActivity(intent)

        }
    }

    fun logout() {
        preferences = context?.getSharedPreferences("Username", 0) as SharedPreferences
        preferences.edit().clear().commit()
        preferences = context?.getSharedPreferences("Avatar",0) as SharedPreferences
        preferences.edit().clear().commit()
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun bindView(rootView: ViewGroup) {
        nameInfo = rootView.findViewById(R.id.name)
        emailInfo = rootView.findViewById(R.id.email)
        logout = rootView.findViewById(R.id.logout)
        progressBar = rootView.findViewById(R.id.progressBar)
        avatarIm = rootView.findViewById(R.id.avatarIm)
        changeAvatarPhoto = rootView.findViewById(R.id.register)
        editor = preferences.edit()
        map = rootView.findViewById(R.id.map)
    }

    private fun initViews() {
        val authorizedName = Singleton.getUserName()
        val authorizedEmail = Singleton.getUserName() + "@mail.ru"
        nameInfo.text = authorizedName
        emailInfo.text = authorizedEmail
        try {
            preferences = context!!.getSharedPreferences("Avatar",0)
            val pathPhotoAvatar = preferences.getString("uri",null)
            avatarIm.setImageURI(Uri.parse(pathPhotoAvatar))
        }catch (e: Exception){
            Toast.makeText(activity as Context,"No avatar photos on local",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cameraGranted = ContextCompat.checkSelfPermission(activity as Context,android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
            val galleryGranted = ContextCompat.checkSelfPermission(activity as Context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            if (cameraGranted && galleryGranted) {
                imageChooserDialog()
            } else {
                requestPermissions(
                    arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    RequestConstants.AVATAR_PERMISSION_REQUEST
                )
            }
        } else {
            imageChooserDialog()
        }
    }

    private fun imageChooserDialog(){
        val adapter: ArrayAdapter<String> = ArrayAdapter(activity as Context, android.R.layout.simple_list_item_1)
        adapter.add("Camera")
        adapter.add("Gallery")
        AlertDialog.Builder(activity as Context)
            .setTitle("Change avatar")
            .setAdapter(adapter) { dialog, which ->
                if (which == 0) {
                    openCamera()
                } else {
                    openGallery()
                }
            }
            .create()
            .show()
    }

    private fun openCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(context!!.packageManager) !=null){
            var photoFile: File? = null
            try{
                photoFile=createImageFile()
            }catch (e: IOException){}
            if(photoFile != null){
                val photoUri = FileProvider.getUriForFile(
                    activity as Context,
                    "${context?.packageName}.provider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun createImageFile():File?{
        val filename = "MyAvatars"
        val storageDir = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(
            filename,
            ".jpg",
            storageDir
        )
        photoPath = image.absolutePath
        return image
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        preferences = context!!.getSharedPreferences("Avatar",0)
        if (resultCode == Activity.RESULT_OK && requestCode==REQUEST_TAKE_PHOTO) {
            preferences.edit().clear().commit()
            preferences.edit().putString("uri",photoPath).commit()
            avatarIm.setImageURI(Uri.parse(photoPath))
            Toast.makeText(activity as Context,"Your photo is changed",Toast.LENGTH_SHORT).show()

        } else if (requestCode == RequestConstants.GALLERY) {
            if(data?.data == null){
                preferences = context!!.getSharedPreferences("Avatar",0)
                val pathPhotoAvatar = preferences.getString("uri",null)
                avatarIm.setImageURI(Uri.parse(pathPhotoAvatar))
                Toast.makeText(activity as Context,"You don't change your photo",Toast.LENGTH_SHORT).show()
            }
            else{
                val image = data?.data
                preferences.edit().clear().commit()
                preferences.edit().putString("uri",image.toString()).commit()
                avatarIm.setImageURI(image)
                Toast.makeText(activity as Context,"Your photo is changed",Toast.LENGTH_SHORT).show()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RequestConstants.AVATAR_CAMERA_PERMISSION_REQUEST) {
            if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
            return
        } else if (requestCode == RequestConstants.AVATAR_GALLERY_PERMISSION_REQUEST) {
            if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            }
            return
        }
        else if (requestCode == RequestConstants.AVATAR_PERMISSION_REQUEST) {
            if (grantResults.size != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                imageChooserDialog()
            }
        }
    }

    private fun openGallery(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        startActivityForResult(intent, RequestConstants.GALLERY)
    }
}