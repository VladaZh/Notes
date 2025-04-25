package com.example.notes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.databinding.EditActivityBinding
import com.example.notes.db.MyDbManager
import com.example.notes.db.MyIntentConstants

class EditActivity : AppCompatActivity() {
    val myDbManager = MyDbManager(this)
    var tempImageUri = "empty"
    val imageRequestsCode = 10
    lateinit var binding: EditActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMyIntents()
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
            contentResolver.takePersistableUriPermission(data?.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    fun onClickAddImage(view: View) {
        binding.mainImageLayout.visibility = View.VISIBLE
        binding.fbAddImage.visibility = View.GONE
        val params = binding.edTitle.layoutParams as ViewGroup.MarginLayoutParams
        params.topMargin = 10
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
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"

        startActivityForResult(intent, imageRequestsCode)
    }
    fun onClickSave(view: View) {
        var myTitle = binding.edTitle.text.toString()
        var myDesc = binding.edDesc.text.toString()
        if (myTitle.isNotEmpty() && myDesc.isNotEmpty()){
            myDbManager.insertToDb(myTitle, myDesc, tempImageUri)
            finish()
        }
    }
    fun onClickBack(view: View){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    fun getMyIntents(){
        val i = intent

        if (i != null){

            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null){

                binding.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                binding.edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty"){

                    binding.mainImageLayout.visibility = View.VISIBLE
                    binding.imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                }
                binding.fbSave.visibility = View.GONE

            }
        }
    }
}