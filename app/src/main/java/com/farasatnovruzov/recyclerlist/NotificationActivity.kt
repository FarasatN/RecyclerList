//package com.farasatnovruzov.recyclerlist
//
//import android.annotation.SuppressLint
//import android.app.SearchManager
//import android.content.Context
//import android.content.Intent
//import android.content.SharedPreferences
//import android.graphics.Color
//import android.os.Bundle
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
//import android.view.ViewTreeObserver
//import android.widget.*
//import androidx.lifecycle.ViewModelProvider
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import az.turanbank.mlb.R
//import az.turanbank.mlb.data.local.AlertDialogMapper
//import az.turanbank.mlb.data.remote.model.response.NotificationResult
//import az.turanbank.mlb.di.ViewModelProviderFactory
//import az.turanbank.mlb.presentation.base.BaseActivity
//import az.turanbank.mlb.presentation.login.activities.pin.LoginPinActivity
//import io.reactivex.rxkotlin.addTo
//import kotlinx.android.synthetic.main.activity_notification.*
//import javax.inject.Inject
//
//
//class NotificationActivity : BaseActivity() {
//
//    lateinit var viewModel: NotificationViewModel
//
//    @Inject
//    lateinit var factory: ViewModelProviderFactory
//    @Inject
//    lateinit var sharedPrefs: SharedPreferences
//
//    lateinit var adapter: NotificationAdapter
//    private var isRefresh = true
//    var isCustomerType: Int = 0
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_notification)
//        setSupportActionBar(toolbar)
//        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
//        toolbar.title = getString(R.string.notification)
//        toolbar.setTitleTextColor(Color.WHITE)
//
//        toolbar.setNavigationOnClickListener(View.OnClickListener { finish() })
//
//        container.isEnabled = isRefresh
//
//        viewModel = ViewModelProvider(this, factory)[NotificationViewModel::class.java]
//
//        isCustomerType = viewModel.isCustomerType
//        /* val prefs = getSharedPreferences("NotificationPref", Context.MODE_PRIVATE)
//         if (!prefs.getBoolean("isActivePin", false)) {
//             Log.d("TAG", "notification: " + prefs.getBoolean("isActivePin", false))
//             var editor = prefs.edit()
//             editor.putString("notification", "Notifications")
//             editor.apply()
//             editor.commit()
//             val intent = Intent(this, LoginPinActivity::class.java)
//             intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.
//
//             dialog.hideDialog()
//             startActivity(intent)
//         }*/
//        val asanLogin = sharedPrefs.getBoolean("loggedInWithAsan", false)
//        val simaLogin = sharedPrefs.getBoolean("loggedInWithSima", false)
//        val isActivePin = sharedPrefs.getBoolean("isActivePin", false)
//
//        if (!isActivePin && !asanLogin && !simaLogin) {
//            Log.i("TAGGGG", "onCreate:notification111 ")
//            var editor = sharedPrefs.edit()
//            editor.putString("notification", "WithUrl")
//            editor.apply()
//            val intent = Intent(this, LoginPinActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//        }else {
//            Log.i("TAGGGG", "onCreate:notification222 ")
//            viewModel.inputs.getNotificationList()
//            setOutputListeners()
//        }
//
//            recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
//            recyclerView.addItemDecoration(
//                DividerItemDecoration(
//                    recyclerView.context,
//                    DividerItemDecoration.VERTICAL
//                )
//            )
//
//            authOpCheckBox.setOnCheckedChangeListener { _, isChecked ->
//                adapter.setAllSelected(isChecked)
//            }
//
//            notificationSelectAllCancel.setOnClickListener {
//                authOpCheckBox.isChecked = false
//                adapter.deselectAll()
//                selectAll.visibility = View.GONE
//
//            }
//
//            container.setOnRefreshListener {
//                viewModel.inputs.getNotificationList()
//            }
//
//
////        authOpCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
////
////
////                adapter.setAllSelected()
////
////        }
//
////        val llm = LinearLayoutManager(this)
////        llm.orientation = LinearLayoutManager.VERTICAL
////
////        recyclerView.layoutManager = llm
////
////        adapter = NotificationAdapter(
////        )
////        recyclerView.adapter = adapter
//    }
//
//    fun isRefreshing(isRefresh: Boolean) {
//        this.isRefresh = isRefresh
//        container.isEnabled = isRefresh
//    }
//
//    fun selectAllVisible(value: Boolean) {
//        if (value) {
//            selectAll.visibility = View.VISIBLE
//        } else {
////            authOpCheckBox.isChecked = false
//            selectAll.visibility = View.GONE
//        }
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        val inflater = menuInflater
//        inflater.inflate(R.menu.notification_menu, menu)
//
//        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
//        val searchItem = menu?.findItem(R.id.search)
//        val searchView = searchItem?.actionView as SearchView
//        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))
//        searchView.isSubmitButtonEnabled = false
//
//
//        searchView.isIconifiedByDefault = false
//        searchView.isFocusable = true
//        searchView.isIconified = false
//        searchView.requestFocusFromTouch()
//
//        val id =
//            searchView.context.resources.getIdentifier("android:id/search_src_text", null, null)
//        val textView = searchView.findViewById<View>(id) as TextView
//        textView.setTextColor(Color.WHITE)
//        textView.setHintTextColor(Color.WHITE)
//        val magId = resources.getIdentifier("android:id/search_mag_icon", null, null)
//        val magImage = searchView.findViewById<View>(magId) as ImageView
//        magImage.visibility = View.GONE
//        magImage.layoutParams = LinearLayout.LayoutParams(0, 0)
//        searchView.findViewById<View>(
//            resources.getIdentifier(
//                "android:id/search_plate",
//                null,
//                null
//            )
//        ).setBackgroundResource(R.drawable.bottom_search_background)
//
//        val searchClose: ImageView = searchView.findViewById(
//            resources.getIdentifier(
//                "android:id/search_close_btn",
//                null,
//                null
//            )
//        )
//        searchClose.setImageResource(R.drawable.ic_baseline_close_24)
//        searchView.queryHint = getString(R.string.action_search)
//        Log.e("error", "3")
//
//        searchClose.setOnClickListener {
//            searchView.clearFocus()
//            searchView.setQuery("", false)
//            searchItem.collapseActionView()
//            informationNotFound.visibility = View.GONE
//        }
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextChange(query: String?): Boolean {
//                adapter.filter.filter(query)
//
//                if (adapter.notificationFilterList.size == 0) {
//                    informationNotFound.visibility = View.VISIBLE
//                } else {
//                    informationNotFound.visibility = View.GONE
//                }
//                return true
//            }
//
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                searchView.clearFocus()
//                searchView.setQuery("", false)
//                searchItem.collapseActionView()
//                Log.e("error", "2")
//                return true
//            }
//        })
//
//        return true
//    }
//
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id: Int = item.itemId
//        if (id == R.id.search) {
//
//            return true
//        } else if (id == R.id.delete) {
//            adapter.selectorDeleteActive(::deleteOperationLmbd)
//            return true
//        } else if (id == R.id.readmark) {
//            adapter.selectorReadActive(::readOperationLmbd)
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    override fun onDestroy() {
//        if (sharedPrefs.getBoolean("isActivePin", false) || sharedPrefs.getBoolean("loggedInWithAsan", false) || sharedPrefs.getBoolean("loggedInWithSima", false)) {
//            var editor = sharedPrefs.edit()
//            editor.putString("notification", "")
//            editor.apply()
//        }
//        super.onDestroy()
//    }
//
//    fun deleteOperationLmbd(list: ArrayList<NotificationResult>): Unit {
//        showDialog_()
//        val listId = ArrayList<Long>()
//        list.forEach {
//            listId.add(it.id)
//        }
//        viewModel.inputs.deleteNotification(listId)
//    }
//
//    fun readOperationLmbd(list: ArrayList<NotificationResult>): Unit {
//        showDialog_()
//        val listId = ArrayList<Long>()
//        list.forEach {
//            listId.add(it.id)
//        }
//        viewModel.inputs.readNotification(listId)
//    }
//
//    fun readOneOperationLmbd(list: ArrayList<NotificationResult>): Unit {
//        val listId = ArrayList<Long>()
//        list.forEach {
//            listId.add(it.id)
//        }
//        viewModel.inputs.readNotification(listId)
//    }
//
//    @SuppressLint("CheckResult")
//    private fun setOutputListeners() {
//        showDialog_()
//        viewModel.outputs.notificationListSuccess().subscribe {
//            if (it.size == 0) {
//                noDataAvailable.visibility = View.VISIBLE
//            }
//
//            adapter = NotificationAdapter(it, this, isCustomerType) {
//
//            }
//            recyclerView.adapter = adapter
//
//            recyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
//                ViewTreeObserver.OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    recyclerView.viewTreeObserver
//                        .removeOnGlobalLayoutListener(this)
//                    hideDialog_()
//                }
//            })
//
//            container.setRefreshing(false)
//        }.addTo(subscriptions)
//
//        viewModel.outputs.notificationDeleteSuccess().subscribe {
//            hideDialog_()
//            Toast.makeText(this, getString(R.string.notification_deleted), Toast.LENGTH_SHORT)
//                .show()
//        }.addTo(subscriptions)
//
//        viewModel.outputs.notificationReadSuccess().subscribe {
//            hideDialog_()
//        }.addTo(subscriptions)
//
//        viewModel.outputs.onError().subscribe {
//            if (it == 338) {
//                val list = ArrayList<NotificationResult>()
//                adapter = NotificationAdapter(list, this, isCustomerType) {
//
//                }
//                recyclerView.adapter = adapter
//                noDataAvailable.visibility = View.VISIBLE
//            } else {
//                AlertDialogMapper(this, it).showAlertDialog()
//            }
//            container.setRefreshing(false)
//            hideDialog_()
//        }.addTo(subscriptions)
//    }
//
//
//}