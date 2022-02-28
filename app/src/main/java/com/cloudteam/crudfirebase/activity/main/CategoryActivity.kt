package com.cloudteam.crudfirebase.activity.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudteam.crudfirebase.R
import com.cloudteam.crudfirebase.activity.addedit.AddEditActivity
import com.cloudteam.crudfirebase.activity.addedit.AddEditActivityCategories
import com.cloudteam.crudfirebase.bind.AllCategoriesAdapter
import com.cloudteam.crudfirebase.model.Categories
import com.cloudteam.crudfirebase.utils.Const
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.activity_main.*


class CategoryActivity : AppCompatActivity() {
    private lateinit var mAdapter: FirestoreRecyclerAdapter<Categories, AllCategoriesAdapter.CategoriesViewHolder>
    private val mFirestore = FirebaseFirestore.getInstance()
    private val mCategoriessCollection = mFirestore.collection(Const.PATH_COLLECTION_CAT)
    private val mQuery = mCategoriessCollection.orderBy(Const.PATH_COLLECTION_NAME)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        initView()
        setupAdapter()

        btn_products2.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() {
        supportActionBar?.title = "CAFETARIA"
        rv_firedbcat.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CategoryActivity)
        }
        fab_firedbcat.setOnClickListener {
            //berpindah ke activity AddEditActivity untuk tambah data
            startActivity(Intent(this, AddEditActivityCategories::class.java).apply {
                putExtra(AddEditActivity.REQ_EDIT, false)
            })
        }
    }

    private fun setupAdapter() {
        //set adapter yang akan menampilkan data pada recyclerview
        val options = FirestoreRecyclerOptions.Builder<Categories>()
            .setQuery(mQuery, Categories::class.java)
            .build()

//        mAdapter = object : FirestoreRecyclerAdapter<Users, UsersViewHolder>(options) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
//                return UsersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false))
//
//
//            override fun onBindViewHolder(viewHolder: UsersViewHolder, position: Int, model: Users) {
//                viewHolder.bindItem(model)
//                viewHolder.itemView.setOnClickListener {
//                    showDialogMenu(model)
//                }
//            }
//        }
        mAdapter = AllCategoriesAdapter(this, mCategoriessCollection, options)
        mAdapter.notifyDataSetChanged()
        rv_firedbcat.adapter = mAdapter
    }

//    private fun showDialogMenu(users: Users) {
//        //dialog popup edit hapus
//        val builder = AlertDialog.Builder(this@MainActivity, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
//        val option = arrayOf("Edit", "Hapus")
//        builder.setItems(option) { dialog, which ->
//            when (which) {
//                //0 -> untuk berpindah ke activity AddEditActivity untuk edit dengan membawa data
//                0 -> startActivity(Intent(this, AddEditActivity::class.java).apply {
//                    putExtra(AddEditActivity.REQ_EDIT, true)
//                    putExtra(AddEditActivity.EXTRA_DATA, users)
//                })
//                1 -> showDialogDel(users.strId)
//            }
//        }
//        builder.create().show()
//    }

//    private fun showDialogDel(strId: String) {
//        //dialog pop delete
//        val builder = AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
//            .setTitle("Hapus Data")
//            .setMessage("Yakin mau hapus?")
//            .setPositiveButton(android.R.string.yes) { dialog, which ->
//                deleteById(strId)
//            }
//            .setNegativeButton(android.R.string.cancel, null)
//        builder.create().show()
//    }

//    private fun deleteById(id: String) {
//        //menghapus data berdasarkan id
//        mUsersCollection.document(id)
//            .delete()
//            .addOnCompleteListener { Toast.makeText(this, "Succes Hapus data", Toast.LENGTH_SHORT).show() }
//            .addOnFailureListener { Toast.makeText(this, "Gagal Hapus data", Toast.LENGTH_SHORT).show() }
//    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

}