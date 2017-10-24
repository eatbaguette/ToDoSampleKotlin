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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Realmのセットアップ
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.deleteRealm(realmConfig)
        mRealm = Realm.getInstance(realmConfig)

        // Realm読み込み
        this.dateList = mRealm.where(TodoModel::class.java).findAll()

        // RecyclerViewのセットアップ
        mRecyclerView = findViewById(R.id.recyclerView_activity_main) as RecyclerView
        mAdapter = RecyclerViewAdapter(dateList)
        val layoutManager = LinearLayoutManager(applicationContext)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.itemAnimator = DefaultItemAnimator()
        mRecyclerView.adapter = mAdapter
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, 1))

        fab_activity_main.setOnClickListener {
            showEditTextDialog(mRealm)
        }
    }

    private fun showEditTextDialog(mRealm: Realm) {
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