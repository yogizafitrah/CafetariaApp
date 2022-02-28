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
import com.cloudteam.crudfirebase.model.Categories
import com.google.firebase.firestore.CollectionReference
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.item_list_category.view.tv_name


class AllCategoriesAdapter (
    private val context: Context,
    private val collection: CollectionReference,
    options: FirestoreRecyclerOptions<Categories>
) : FirestoreRecyclerAdapter<Categories, AllCategoriesAdapter.CategoriesViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_list_category, parent, false))
    }

    override fun onBindViewHolder(viewHolder: CategoriesViewHolder, position: Int, category: Categories) {
        viewHolder.bindItem(category)
        viewHolder.itemView.setOnClickListener {
            showDialogMenu(category)
        }
    }
    class CategoriesViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(category: Categories) {
            view.apply {
                val name = " ${category.nameCategory}"

                tv_name.text = name
            }
        }
    }

    private fun showDialogMenu(category: Categories) {
        //dialog popup edit hapus
        val builder = AlertDialog.Builder(context, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
        val option = arrayOf("Edit", "Hapus")
        builder.setItems(option) { dialog, which ->
            when (which) {
                //0 -> untuk berpindah ke activity AddEditActivity untuk edit dengan membawa data
                0 -> context.startActivity(Intent(context, AddEditActivity::class.java).apply {
                    putExtra(AddEditActivity.REQ_EDIT, true)
                    putExtra(AddEditActivity.EXTRA_DATA, category)
                })
                1 -> showDialogDel(category.strId)
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