package com.cloudteam.crudfirebase.activity.addedit

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cloudteam.crudfirebase.R
import com.cloudteam.crudfirebase.model.Categories
import com.cloudteam.crudfirebase.utils.Const
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_addedit.*

class AddEditActivityCategories : AppCompatActivity() {
    companion object {
        //key untuk intent data
        const val EXTRA_DATA = "extra_data"
        const val REQ_EDIT = "req_edit"
    }

    private var isEdit = false
    private var categories: Categories? = null
    private val mFirestore = FirebaseFirestore.getInstance()
    private val mUsersCollection = mFirestore.collection(Const.PATH_COLLECTION_CAT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addedit_category)
        //mengambil data yang dibawa dari mainactivity sesuia keynya masing2
        isEdit = intent.getBooleanExtra(REQ_EDIT, false)
        categories = intent.getParcelableExtra(EXTRA_DATA)

        btn_save.setOnClickListener { saveData() }
        initView()
    }

    private fun initView() {
        //set view jika data di edit maka akan tampil pada form input
        if (isEdit) {
            btn_save.text = getString(R.string.update)
            ti_name.text = Editable.Factory.getInstance().newEditable(categories?.nameCategory)
        }
    }

    private fun saveData() {
        setData(categories?.strId)
    }

    private fun setData(strId: String?) {
        createCategories(strId).addOnCompleteListener {
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
    private fun createCategories(strId: String?): Task<Void> {
        val writeBatch = mFirestore.batch()
        val path = Const.PATH_COLLECTION_CAT + Const.setTimeStamp().toString() //exmp hasil : users-43287845
        val id = strId ?: path
        val name = ti_name.text.toString()

        val categories = Categories(id, name)
        writeBatch.set(mUsersCollection.document(id), categories) //menyimpan data dengan id yang sudah ditentukan
        return writeBatch.commit()
    }
}