package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout

class SearchActivity : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    companion object {
        const val SEARCH_TEXT = "searchText"
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val searchText = searchEditText.text.toString()
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val searchText = savedInstanceState.getString(SEARCH_TEXT)
        searchEditText.setText(searchText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        val currentView = this.window.decorView.rootView


        if (savedInstanceState != null) {
            val searchText = savedInstanceState.getString(SEARCH_TEXT)
            searchEditText.setText(searchText)
        }


        val buttonBack: LinearLayout = findViewById<LinearLayout>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish()
        }

        clearButton.setOnClickListener {
            searchEditText.setText("")
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(currentView.windowToken, 0)
        }

        fun clearButtonVisibility(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE }
        }

        fun isPrime(number: Int): Boolean {
            for (i in 2..number / 2) {
                if (number % i == 0) {
                    return false } }
            return true }

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    clearButton.visibility = View.GONE
                } else {
                    clearButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

}
