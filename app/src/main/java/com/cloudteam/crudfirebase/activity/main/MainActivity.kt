package com.cloudteam.crudfirebase.activity.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.cloudteam.crudfirebase.R
import com.cloudteam.crudfirebase.activity.addedit.AddEditActivity
import com.cloudteam.crudfirebase.bind.AllProductsAdapter
import com.cloudteam.crudfirebase.model.Products
import com.cloudteam.crudfirebase.utils.Const.PATH_COLLECTION
import com.cloudteam.crudfirebase.utils.Const.PATH_PRICE
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mSearchText : EditText
    private lateinit var mAdapter: FirestoreRecyclerAdapter<Products, AllProductsAdapter.ProductsViewHolder>
    private val mFirestore = FirebaseFirestore.getInstance()
    private val mProductsCollection = mFirestore.collection(PATH_COLLECTION)
    private val mQuery = mProductsCollection.orderBy(PATH_PRICE, Query.Direction.ASCENDING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        mSearchText = findViewById(R.id.edt_search)
        initView()
        setupAdapter()

        btn_category.setOnClickListener{
            val intent = Intent(this,CategoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initView() {
        supportActionBar?.title = "CAFETARIA"
        rv_firedb.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        fab_firedb.setOnClickListener {
            //berpindah ke activity AddEditActivity untuk tambah data
            startActivity(Intent(this, AddEditActivity::class.java).apply {
                putExtra(AddEditActivity.REQ_EDIT, false)
            })
        }
    }


    private fun setupAdapter() {

        //set adapter yang akan menampilkan data pada recyclerview
        val options = FirestoreRecyclerOptions.Builder<Products>()
            .setQuery(mQuery, Products::class.java)
            .build()

//        mAdapter = object : FirestoreRecyclerAdapter<Users, UsersViewHolder>(options) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
//                return UsersViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_user, parent, false))
//            }
//
//            override fun onBindViewHolder(viewHolder: UsersViewHolder, position: Int, model: Users) {
//                viewHolder.bindItem(model)
//                viewHolder.itemView.setOnClickListener {
//                    showDialogMenu(model)
//                }
//            }
//        }
        mAdapter = AllProductsAdapter(this, mProductsCollection, options)
        mAdapter.notifyDataSetChanged()
        rv_firedb.adapter = mAdapter
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