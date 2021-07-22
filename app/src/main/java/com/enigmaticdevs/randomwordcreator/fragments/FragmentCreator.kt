package com.enigmaticdevs.randomwordcreator.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bhavita.toasts.Toasts
import com.enigmaticdevs.randomwordcreator.R
import com.enigmaticdevs.randomwordcreator.databinding.FragmentCreatorBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.maximeroussy.invitrode.WordGenerator
import java.util.*

class FragmentCreator : Fragment() {
    private var twoWords: Boolean = false
    private var wordLength: Boolean = false
    private var length : Int = 3
    private var adCount = 0
    private val generator = WordGenerator()
    private val wordStack = Stack<String>()
    private val default = "RANDOM"
    private lateinit var sqLiteDatabase: SQLiteDatabase
    private lateinit var mInterstitialAd: InterstitialAd
    @SuppressLint("Recycle")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_creator, container, false)
        val binding = FragmentCreatorBinding.bind(view)
        val sharedPreferences = context!!.getSharedPreferences(
            R.string.package_name.toString(),
            Context.MODE_PRIVATE
        )
        sharedPreferences.edit().putInt("state", 10).apply()
        try {
            sqLiteDatabase = context!!.openOrCreateDatabase("words", Context.MODE_PRIVATE, null)
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS favorite(id INTEGER ,name VARCHAR)")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        wordStack.push(default)
        initListener(binding)
        binding.refreshButton.setOnClickListener {
            if (!wordLength && !twoWords) {
                val random = (3..12).random()
                binding.random.text = generator.newWord(random)
                wordStack.push(binding.random.text.toString())
            }
            if (binding.twoWordsCheckbox.isChecked) {
                if (binding.wordLengthCheckbox.isChecked) {
                    generateWord(length, binding)

                } else {
                    val random = (3..12).random()
                    generateWord(random, binding)
                }
            }
            else
            if(binding.wordLengthCheckbox.isChecked)
            {
                binding.random.text = generator.newWord(length)
                wordStack.push(binding.random.text.toString())
            }
            if (adCount > 12) {
                if (mInterstitialAd.isLoaded) {
                    mInterstitialAd.show()
                    adCount = 0
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.")
                }
            }
        }
        binding.previousButton.setOnClickListener{
            if (wordStack.size>1) {
                wordStack.pop()
                binding.random.text = wordStack.lastElement()
            }
        }
        binding.favoriteButton.setOnClickListener{
            val c: Cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM favorite WHERE name='" + binding.random.text.toString() + "'",
                null
            )
            if (c.moveToFirst()) {
                Log.i("Error", "Record exist")
                Toasts.c(
                    context!!,
                    getString(R.string.already_saved),
                    Toast.LENGTH_LONG,
                    backgroundColor = R.attr.tintColor
                ).show()
            } else {
                val sql = "INSERT INTO favorite(name) VALUES(?)"
                val statement = sqLiteDatabase.compileStatement(sql)
                statement.bindString(1, binding.random.text.toString())
                statement.execute()
                Log.i("DataSaved", "Done")
                Toasts.c(
                    context!!,
                    getString(R.string.saved),
                    Toast.LENGTH_LONG,
                    backgroundColor = R.attr.tintColor
                ).show()
                if (sharedPreferences.getInt("state", 0) == 0) {
                    sharedPreferences.edit().putInt("state", 1).apply()
                } else {
                    sharedPreferences.edit().putInt("state", 0).apply()
                }
            }

        }
        mInterstitialAd = InterstitialAd(context)
        mInterstitialAd.adUnitId = "ca-app-pub-7745746346619481/1854143856"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(AdRequest.Builder().build())
            }
        }
        return view

    }

    private fun generateWord(length: Int, binding: FragmentCreatorBinding) {
        val second = generator.newWord(length)
        val first = generator.newWord(length)
        val word = "$first $second"
        binding.random.text = word
        wordStack.push(word)
    }

    private fun initListener(binding: FragmentCreatorBinding) {
        binding.wordLimit.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                length = p1 + 3
                binding.wordLengthText.text = length.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

}
