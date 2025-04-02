package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.TypedValueCompat.dpToPx
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.databinding.EditActivityBinding
import com.example.notes.db.MyDbManager

class EditActivity : AppCompatActivity() {
    val myDbManager = MyDbManager(this)
    var tempImageUri = "empty"
    val imageRequestsCode = 10
    lateinit var binding: EditActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == imageRequestsCode){
            binding.imMainImage.setImageURI(data?.data)
            tempImageUri = data?.data.toString()
        }
    }

    fun onClickAddImage(view: View) {
        binding.mainImageLayout.visibility = View.VISIBLE
        binding.fbAddImage.visibility = View.GONE
        val params = binding.edTitle.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = 10//
        binding.edTitle.layoutParams = params
    }

    fun onClickDeleteImage(view: View) {
        binding.mainImageLayout.visibility = View.GONE
        binding.fbAddImage.visibility = View.VISIBLE
        val params = binding.edTitle.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = 90
        binding.edTitle.layoutParams = params
    }

    fun onClickChooseImage(view: View) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, imageRequestsCode)
    }
    fun onClickSave(view: View) {
        val myTitle = binding.edTitle.text.toString()
        val myDesc = binding.edDesc.text.toString()
        if (myTitle !== "" || myDesc != ""){
            myDbManager.insertToDb(myTitle, myDesc, tempImageUri)
        }
    }
}