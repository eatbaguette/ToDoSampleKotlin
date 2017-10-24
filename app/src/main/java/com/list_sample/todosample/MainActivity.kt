package com.list_sample.todosample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab_activity_main.setOnClickListener {
            Toast.makeText(this, "hoge", Toast.LENGTH_LONG).show()
        }
    }
}
