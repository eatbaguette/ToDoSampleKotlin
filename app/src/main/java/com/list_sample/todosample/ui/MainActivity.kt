package com.list_sample.todosample.ui

import android.app.Dialog
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.list_sample.todosample.R
import com.list_sample.todosample.adapter.RecyclerViewAdapter
import com.list_sample.todosample.model.TodoModel
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mRealm: Realm
    lateinit var mRecyclerView: RecyclerView
    lateinit var mAdapter: RecyclerViewAdapter
    lateinit var dateList: RealmResults<TodoModel>

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Realmのセットアップ
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        mRealm = Realm.getInstance(realmConfig)

        // Realm読み込み
        this.dateList = mRealm.where(TodoModel::class.java).findAll()

        // RecyclerViewのセットアップ
        mAdapter = RecyclerViewAdapter(dateList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView_activity_main.layoutManager = layoutManager
        recyclerView_activity_main.itemAnimator = DefaultItemAnimator()
        recyclerView_activity_main.adapter = mAdapter
        recyclerView_activity_main.addItemDecoration(DividerItemDecoration(this, 1))

        fab_activity_main.setOnClickListener {
            showEditTextDialog()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, this.dateList.toString())
    }


    private fun showEditTextDialog() {
        val editText = EditText(this)
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(R.string.alert_dialog_title)
        dialog.setView(editText)
        dialog.setPositiveButton(R.string.alert_dialog_ok, DialogInterface.OnClickListener { dialogInterface, i ->
            mRealm.executeTransaction {
                val todoModel = this.mRealm.createObject(TodoModel::class.java)
                todoModel.todo = editText?.text.toString()
                mRealm.copyToRealm(todoModel)
            }
            mAdapter.notifyDataSetChanged()
        })

        dialog.show()
    }
}