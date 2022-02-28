package com.cloudteam.crudfirebase.activity.addedit

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cloudteam.crudfirebase.R
import com.cloudteam.crudfirebase.model.Products
import com.cloudteam.crudfirebase.utils.Const.PATH_COLLECTION
import com.cloudteam.crudfirebase.utils.Const.setTimeStamp
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import kotlinx.android.synthetic.main.activity_addedit.*


class AddEditActivity : AppCompatActivity() {
    companion object {
        //key untuk intent data
        const val EXTRA_DATA = "extra_data"
        const val REQ_EDIT = "req_edit"
    }

//    private var mImageUri : Uri? = null
    private var isEdit = false
    private var products: Products? = null
    private val mFirestore = FirebaseFirestore.getInstance()
    private val mUsersCollection = mFirestore.collection(PATH_COLLECTION)
//    private var mStorageRef: StorageReference? = FirebaseStorage.getInstance().getReference("images")
//    private var mUploadTask: StorageTask<*>? = null
//    private val PICK_IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addedit)
        //mengambil data yang dibawa dari mainactivity sesuia keynya masing2
        isEdit = intent.getBooleanExtra(REQ_EDIT, false)
        products = intent.getParcelableExtra(EXTRA_DATA)

        btn_save.setOnClickListener { saveData() }
        initView()
    }

    private fun initView() {
        //set view jika data di edit maka akan tampil pada form input
        if (isEdit) {
            btn_save.text = getString(R.string.update)
            ti_name.text = Editable.Factory.getInstance().newEditable(products?.strName)
            ti_category.text = Editable.Factory.getInstance().newEditable(products?.strCategory)
            ti_price.text = Editable.Factory.getInstance().newEditable(products?.doublePrice.toString())
        }
    }


    private fun saveData() {
        setData(products?.strId)
    }

    private fun setData(strId: String?) {
        createProducts(strId).addOnCompleteListener {
            if (it.isSuccessful) {
                if (isEdit) {
                    Toast.makeText(this, "Sukses perbarui data", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Sukses tambah data", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Gagal tambah data", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Error Added data ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    //fungsi untuk mengambil inputan data dan menyimpannya pada firestore
    private fun createProducts(strId: String?): Task<Void> {
        val writeBatch = mFirestore.batch()
        val path = PATH_COLLECTION + setTimeStamp().toString() //exmp hasil
        val id = strId ?: path
        val name = ti_name.text.toString()
        val category = ti_category.text.toString()
        val price = ti_price.text.toString()

        val products = Products(id, name, category, price.toFloat())
        writeBatch.set(mUsersCollection.document(id), products) //menyimpan data dengan id yang sudah ditentukan
        return writeBatch.commit()
    }
}