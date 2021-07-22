package com.enigmaticdevs.randomwordcreator.fragments

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enigmaticdevs.randomwordcreator.R
import com.enigmaticdevs.randomwordcreator.adapters.ItemAdapter
import com.enigmaticdevs.randomwordcreator.databinding.FragmentSavedBinding

class FragmentSaved : Fragment() {
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private lateinit var sharedPreferences: SharedPreferences
    private var likedWords :MutableList<String> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: ItemAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved, container, false)
        sharedPreferences = context!!.getSharedPreferences(
            R.string.package_name.toString(),
            Context.MODE_PRIVATE
        )
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
        recyclerView = view.findViewById(R.id.favorite_recyclerview)
        itemAdapter = ItemAdapter(likedWords, context!!)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = itemAdapter
        readData()
        return view
    }

    private fun readData() {
          try {
            sqLiteDatabase = context!!.openOrCreateDatabase("words", Context.MODE_PRIVATE, null)
            val c = sqLiteDatabase.rawQuery("SELECT * FROM favorite", null)
            val nameIndex = c!!.getColumnIndex("name")
            c.moveToFirst()
            while (true) {
                Log.i("Name", c.getString(nameIndex))
                likedWords.add(0, c.getString(nameIndex))
                itemAdapter.notifyDataSetChanged()
                c.moveToNext()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private var listener = OnSharedPreferenceChangeListener { _: SharedPreferences?, _: String? ->
        Log.i("Changed", "Changed")
        likedWords.clear()
        itemAdapter.notifyDataSetChanged()
        try {
            sqLiteDatabase = context!!.openOrCreateDatabase(
                "words",
                Context.MODE_PRIVATE,
                null
            )
            val c: Cursor =
                sqLiteDatabase.rawQuery("SELECT * FROM favorite", null)
            val nameIndex = c.getColumnIndex("name")
            c.moveToFirst()
            while (true) {
                Log.i("Name", c.getString(nameIndex))
                likedWords.add(0, c.getString(nameIndex))
                itemAdapter.notifyDataSetChanged()
                c.moveToNext()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}