package com.list_sample.todosample.ui

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
import android.view.Menu
import android.view.View
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
    private lateinit var mRealm: Realm
    private lateinit var mAdapter: RecyclerViewAdapter
    private lateinit var todoList: RealmResults<TodoModel>
    private lateinit var incrementalSearchAdapter: RecyclerViewAdapter
    private var incrementalSearchQuery = ""

    private val TAG = "MainActivity"
    private val CREATE_NEW_TODO = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ツールバーのセットアップ
        toolbar_activity_main.inflateMenu(R.menu.search_menu)
        setSupportActionBar(toolbar_activity_main)


        // Realmのセットアップ
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build()
        mRealm = Realm.getInstance(realmConfig)

        // Realm読み込み
        this.todoList = mRealm.where(TodoModel::class.java).findAll()

        // RecyclerViewのセットアップ
        mAdapter = RecyclerViewAdapter(todoList)
        incrementalSearchAdapter = RecyclerViewAdapter(todoList) //notifyDatasetChangedで落ちるので初期化しておく。

        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView_activity_main.layoutManager = layoutManager
        recyclerView_activity_main.itemAnimator = DefaultItemAnimator()
        recyclerView_activity_main.adapter = mAdapter
        recyclerView_activity_main.addItemDecoration(DividerItemDecoration(this, 1))
        mAdapter.setOnItemClickListener(onItemClickListener)

        // swipe to dismiss
        val itemTouchHelper = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.RIGHT, ItemTouchHelper.RIGHT) {
                    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                        val position = viewHolder?.adapterPosition ?: return

                        mRealm.executeTransaction {
                            todoList.deleteFromRealm(position)
                        }
                        mAdapter!!.notifyItemRemoved(position)
                    }
                }
        )
        itemTouchHelper.attachToRecyclerView(recyclerView_activity_main)


        fab_activity_main.setOnClickListener {
            showEditTextDialog(CREATE_NEW_TODO)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu!!.findItem(R.id.search_view)
        val searchView = searchItem.actionView as android.support.v7.widget.SearchView
        searchView.queryHint = "することを検索"

        searchView.setOnQueryTextListener(object: android.support.v7.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                return true
            }

            // SearchViewに文字が入力されたらAdapterをincrementalSearchAdapterに差し替え
            // Adapterを差し替えないとRecyclerViewがnotifyDatasetChangedで更新されないため
            override fun onQueryTextChange(newText: String): Boolean {
                if (!(newText.matches("\\s+".toRegex()))) {

                    todoList = mRealm.where(TodoModel::class.java).like("todo", "*$newText*").findAll()

                    incrementalSearchQuery = newText

                    incrementalSearchAdapter = RecyclerViewAdapter(todoList)
                    recyclerView_activity_main.swapAdapter(incrementalSearchAdapter, false)
                    incrementalSearchAdapter.setOnItemClickListener(onItemClickListener)
                }
                return true
            }
        })
        mAdapter.notifyDataSetChanged()
        return true
    }

    // SearchViewが閉じたらAdapterをmAdapterに戻しておく。
    override fun onOptionsMenuClosed(menu: Menu?) {
        super.onOptionsMenuClosed(menu)
        recyclerView_activity_main.swapAdapter(mAdapter, false)
    }

    /**
     * Todoを新規作成、編集するダイアログを出す。
     * @param todoItemNumber Todoを編集する際にタップされたアイテムのポジション。新規作成の場合はCREATE_NEW_TODO(-1)が入る
     */
    private fun showEditTextDialog(todoItemNumber: Int) {
        val editText = EditText(this)
        val dialog = AlertDialog.Builder(this)

        if (todoItemNumber != CREATE_NEW_TODO) editText.setText(mRealm.where(TodoModel::class.java).like(getString(R.string.todo), "*$incrementalSearchQuery*").findAll()[todoItemNumber].todo)

        dialog.setTitle(R.string.alert_dialog_title)
        dialog.setView(editText)
        dialog.setPositiveButton(R.string.alert_dialog_ok, DialogInterface.OnClickListener { dialogInterface, i ->
            mRealm.executeTransaction {

                // 空文字の場合と長さが0の場合は保存しない
                if ((editText?.text.toString().length != 0) and !(editText.text.toString().matches("\\s+".toRegex()))) {

                    // 新規作成なら保存。それ以外なら既存の場所に作成
                    if (todoItemNumber == CREATE_NEW_TODO) {
                        val todoModel = this.mRealm.createObject(TodoModel::class.java)
                        todoModel.todo = editText?.text.toString()
                        mRealm.copyToRealm(todoModel)
                    } else {
                        mRealm.where(TodoModel::class.java).like(getString(R.string.todo), "*$incrementalSearchQuery*").findAll()[todoItemNumber].todo = editText.text.toString()
                    }

                } else {
                    Toast.makeText(this, R.string.toast_null_cannot_save, Toast.LENGTH_SHORT).show()
                }
            }
            incrementalSearchAdapter.notifyDataSetChanged()
            mAdapter?.notifyDataSetChanged()
        })

        dialog.show()
    }

    // クリック時の処理を書く
    private val onItemClickListener = object : RecyclerViewAdapter.OnItemClickListener {
        override fun onItemClick(view: View, position: Int) {
            showEditTextDialog(position)
        }
    }
}