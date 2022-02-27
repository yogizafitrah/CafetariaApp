package com.cloudteam.crudfirebase.bind

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.cloudteam.crudfirebase.R
import com.cloudteam.crudfirebase.activity.addedit.AddEditActivity
import com.cloudteam.crudfirebase.model.Products
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.CollectionReference
import kotlinx.android.synthetic.main.item_list_products.view.*


/**
 * Created by rivaldy on 2/1/2020.
 * Find me on my lol Github :D -> https://github.com/im-o
 */

class AllProductsAdapter(
    private val context: Context,
    private val collection: CollectionReference,
    options: FirestoreRecyclerOptions<Products>
    ) : FirestoreRecyclerAdapter<Products, AllProductsAdapter.ProductsViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_products, parent, false))
    }

    override fun onBindViewHolder(viewHolder: ProductsViewHolder, position: Int, products: Products) {
        viewHolder.bindItem(products)
        viewHolder.itemView.setOnClickListener {
            showDialogMenu(products)
        }
    }
    class ProductsViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(products: Products) {
            view.apply {
                val name = "Product Name   : ${products.strName}"
                val category = "Category : ${products.strCategory}"
                val price = "Price    : $${products.doublePrice}"

                tv_name.text = name
                tv_category.text = category
                tv_price.text = price
            }
        }
    }

    private fun showDialogMenu(products: Products) {
        //dialog popup edit hapus
        val builder = AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        val option = arrayOf("Edit", "Hapus")
        builder.setItems(option) { dialog, which ->
            when (which) {
                //0 -> untuk berpindah ke activity AddEditActivity untuk edit dengan membawa data
                0 -> context.startActivity(Intent(context, AddEditActivity::class.java).apply {
                    putExtra(AddEditActivity.REQ_EDIT, true)
                    putExtra(AddEditActivity.EXTRA_DATA, products)
                })
                1 -> showDialogDel(products.strId)
            }
        }
        builder.create().show()
    }
    private fun showDialogDel(strId: String?) {
        //dialog pop delete
        val builder = AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
            .setTitle("Hapus Data")
            .setMessage("Yakin mau hapus?")
            .setPositiveButton(android.R.string.yes) { dialog, which ->
                deleteById(strId.toString())
            }
            .setNegativeButton(android.R.string.cancel, null)
        builder.create().show()
    }
    private fun deleteById(id: String) {
        //menghapus data berdasarkan id
        collection.document(id)
            .delete()
            .addOnCompleteListener { Toast.makeText(context, "Succes Hapus data", Toast.LENGTH_SHORT).show() }
            .addOnFailureListener { Toast.makeText(context, "Gagal Hapus data", Toast.LENGTH_SHORT).show() }
    }

}