package com.example.tamiltranslatoronboarding

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class StarredActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listView = ListView(this)
        setContentView(listView)

        val prefs = getSharedPreferences("starred", MODE_PRIVATE)
        val items = prefs.getStringSet("items", emptySet())!!.toList()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            items
        )

        listView.adapter = adapter
    }
}