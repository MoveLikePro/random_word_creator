package com.enigmaticdevs.randomwordcreator.adapters

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.enigmaticdevs.randomwordcreator.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class ItemAdapter(private val likedWords: MutableList<String>,private val context: Context) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val word : TextView = itemView.findViewById(R.id.liked_word)
        val delete : ImageView = itemView.findViewById(R.id.delete_button)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_favorite,
            parent,
            false
        )
        val vh = ViewHolder(inflatedView)
        vh.delete.setOnClickListener{
            Log.i("Long", "Long")
            val snackbar = Snackbar.make(
                inflatedView,
                "Delete " + likedWords[vh.adapterPosition] + "?",
                BaseTransientBottomBar.LENGTH_LONG
            )
            snackbar.setAction("Yes") {
                val sqLiteDatabase: SQLiteDatabase = context.openOrCreateDatabase(
                    "words",
                    Context.MODE_PRIVATE,
                    null
                )
                sqLiteDatabase.execSQL(
                    "DELETE FROM favorite WHERE name='" + likedWords[vh.adapterPosition] + "'"
                )
                likedWords.removeAt(vh.adapterPosition)
                notifyDataSetChanged()
            }
            snackbar.show()
        }
        return vh
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.word.text = likedWords[position]
    }

    override fun getItemCount(): Int {
        return likedWords.size
    }
}