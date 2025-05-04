package com.example.notes

import android.content.Intent
import android.icu.util.Calendar
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.notes.databinding.EditActivityBinding
import com.example.notes.db.MyDbManager
import com.example.notes.db.MyIntentConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class EditActivity : AppCompatActivity() {
    val myDbManager = MyDbManager(this)
    var tempImageUri = "empty"
    val imageRequestsCode = 10
    var id = 0
    var isEditState = false
    lateinit var binding: EditActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMyIntents()
        Log.d("timeLog", "Time ${getCurrentTime()}")
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
        binding.imButtonEditImage.visibility = View.VISIBLE
        binding.imButtonDeleteImage.visibility = View.VISIBLE
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

            CoroutineScope(Dispatchers.Main).launch {

                if (isEditState){
                    myDbManager.updateItem(myTitle, myDesc, tempImageUri, id, getCurrentTime())
                } else {
                    myDbManager.insertToDb(myTitle, myDesc, tempImageUri, getCurrentTime())
                }

                finish()
            }

        }
    }
    fun onClickBack(view: View){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    fun getMyIntents(){
        val i = intent
        binding.fbEdit.visibility = View.GONE
        binding.imButtonEditImage.visibility = View.GONE
        binding.imButtonDeleteImage.visibility = View.GONE

        if (i != null){

            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null){

                binding.edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                isEditState = true
                binding.edTitle.isEnabled = false
                binding.edDesc.isEnabled = false
                binding.fbEdit.visibility = View.VISIBLE
                binding.edDesc.setText(i.getStringExtra(MyIntentConstants.I_DESC_KEY))
                id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
                if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty"){

                    binding.mainImageLayout.visibility = View.VISIBLE

                    binding.imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                }
                binding.fbSave.visibility = View.GONE

            }
        }
    }

    fun onEditEnable(view: View) {
        binding.edTitle.isEnabled = true
        binding.edDesc.isEnabled = true
        binding.fbSave.visibility = View.VISIBLE
        binding.fbEdit.visibility = View.GONE
        binding.imButtonEditImage.visibility = View.VISIBLE
        binding.imButtonDeleteImage.visibility = View.VISIBLE

    }
    private fun getCurrentTime():String{
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        val fTime = formatter.format(time)
        return fTime.toString()
    }

}