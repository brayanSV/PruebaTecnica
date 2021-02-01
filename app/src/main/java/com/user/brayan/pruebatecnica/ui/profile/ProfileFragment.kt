package com.user.brayan.pruebatecnica.ui.profile

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.user.brayan.pruebatecnica.BuildConfig
import com.user.brayan.pruebatecnica.ConexionDB.ConexionDBHelper
import com.user.brayan.pruebatecnica.ConexionDB.Perfiles
import com.user.brayan.pruebatecnica.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.bottom_sheet_layout.view.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class ProfileFragment : Fragment(), View.OnClickListener {
    private var _ID : Long = 0
    private lateinit var photoURI : Uri
    private lateinit var imgPerson : CircleImageView
    private lateinit var imgCamera : CircleImageView
    private lateinit var tilName : TextInputLayout
    private lateinit var tilSurName : TextInputLayout
    private lateinit var btnGuardar : MaterialButton
    private lateinit var bottomSheetDialog : BottomSheetDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_perfil, container, false)

        tilName = root.findViewById(R.id.tilName)
        tilSurName = root.findViewById(R.id.tilSurName)

        imgPerson = root.findViewById(R.id.imgPerson)
        imgCamera = root.findViewById(R.id.imgCamera)
        btnGuardar = root.findViewById(R.id.btnGuardar)

        imgPerson.setOnClickListener(this)
        imgCamera.setOnClickListener(this)
        btnGuardar.setOnClickListener(this)

        val viewSheet = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog = BottomSheetDialog(requireContext())
        bottomSheetDialog.setContentView(viewSheet)
        viewSheet.gallery.setOnClickListener(this)
        viewSheet.camara.setOnClickListener(this)

        return root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onClick(view: View?) {
         when (view?.id){
             R.id.imgPerson,
             R.id.imgCamera -> {
                 bottomSheetDialog.show()
             }
             R.id.gallery -> {

             }
             R.id.camara -> {
                 takePhoto()
             }
             R.id.btnGuardar -> {

             }
         }
    }

    private fun takePhoto() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var pathPhoto = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image = File.createTempFile("image", ".jpeg", pathPhoto)
        photoURI = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID.toString() + ".fileprovider",
            image
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(takePictureIntent, 12)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 12) {

        }
    }
}