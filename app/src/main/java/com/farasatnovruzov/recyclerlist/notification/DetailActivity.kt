package com.farasatnovruzov.recyclerlist.notification

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.farasatnovruzov.recyclerlist.R

class DetailActivity() : AppCompatActivity() {

//    var adapter: ItemsRecyclerAdapter = ItemsRecyclerAdapter(context = context,null,
//            dataList = mutableListOf())
    var position = 0
//    init {
//        adapter = itemsRecyclerAdapter
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.title = "Notification"
        toolbar.setTitleTextColor(Color.WHITE)
        toolbar.setNavigationOnClickListener(View.OnClickListener { finish() })

//        itemsRecyclerAdapter = ItemsRecyclerAdapter(context = this.applicationContext,null,
//            dataList = list)

        val titleText = findViewById<TextView>(R.id.titleText)
        val contentText = findViewById<TextView>(R.id.contentText)

        val title = intent.getStringExtra("title")
        val content = intent.getStringExtra("content")
        position = intent.getIntExtra("position",0)

        titleText.text = title
        contentText.text = content

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.detail_menu, menu)

        val deleteItem: MenuItem = menu!!.findItem(R.id.action_delete_detail)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_delete_detail -> {
                val alertDialogBuilder = AlertDialog.Builder(this,
                    androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert)
                alertDialogBuilder.apply {
                    setTitle("Delete notification")
                    setMessage("Are you sure you want to delete the notification?")
                    setPositiveButton("Yes") { dialog, which ->
                        val activity = MainActivity()
                        activity.itemsRecyclerAdapter.removeItem(position)
//                        adapter.removeItem(position)
                        Toast.makeText(applicationContext,"Notification deleted", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }
                    create().setCancelable(false)
                }.show()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}