package com.muod.calldiary.activity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import io.realm.Realm
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.dialog_add_notes.*

import com.muod.calldiary.R
import com.muod.calldiary.adapter.CallListAdapter
import com.muod.calldiary.callback.DoneCallback
import com.muod.calldiary.model.CallObject
import android.provider.ContactsContract
import android.net.Uri.withAppendedPath
import com.muod.calldiary.extra.PermissionUtils


class MainActivity : AppCompatActivity(),DoneCallback,PermissionUtils.PermissionAskListener {


    lateinit var recyclerView:RecyclerView
    lateinit var completedListRecyclerView:RecyclerView
    lateinit var pendingAdapter:CallListAdapter
    lateinit var completedAdapter:CallListAdapter
    lateinit var items:ArrayList<CallObject>
    lateinit var itemsCompleted:ArrayList<CallObject>
    lateinit var layoutManager: LinearLayoutManager
    lateinit var completedLayout: View
    lateinit var completedTextView:TextView
    lateinit var expandImageButton:ImageButton

    var isShowingCompletedItems = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()

//        checkPermissionsUtility()

        items = ArrayList()
        itemsCompleted = ArrayList()
        recyclerView = callerListRecyclerView
        completedListRecyclerView = findViewById(R.id.completedListRecyclerView)
        completedLayout = findViewById(R.id.completedLinearLayout)
        expandImageButton = findViewById(R.id.expansionImageButton)
        completedTextView =findViewById(R.id.completedTextView)
        pendingAdapter = CallListAdapter(this,items)
        completedAdapter = CallListAdapter(this,itemsCompleted)
        layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = pendingAdapter

        completedListRecyclerView.layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        completedListRecyclerView.adapter = completedAdapter

        completedLayout.setOnClickListener {
            if(isShowingCompletedItems){
                completedListRecyclerView.visibility = View.GONE
                expandImageButton.isActivated = false
                isShowingCompletedItems = false
            }
            else{
                completedListRecyclerView.visibility = View.VISIBLE
                expandImageButton.isActivated = true
                isShowingCompletedItems = true
            }
        }

        loadItems()

    }

    fun loadItems(){
        Realm.init(this)
        var realm = Realm.getDefaultInstance()

        var realmObjects = realm.where(CallObject::class.java).equalTo("isDone",false).findAll()
        items.addAll(realmObjects)
        pendingAdapter.notifyDataSetChanged()

        var realmObjectsCompleted = realm.where(CallObject::class.java).equalTo("isDone",true).findAll()
        itemsCompleted.addAll(realmObjectsCompleted)
        completedAdapter.notifyDataSetChanged()
        completedTextView.text = "Completed( "+itemsCompleted.size +" )"
    }

    override fun refreshItems() {

    }

    override fun doneItem(item: CallObject) {
        val index = items.indexOf(item)
        var handler = Handler()
        items.remove(item)

        handler.postDelayed({
            pendingAdapter.notifyDataSetChanged()
        },500)

        var realm = Realm.getDefaultInstance()

        runOnUiThread(Runnable {

            realm.executeTransaction {
                item.isDone = true
                itemsCompleted.add(item)

                handler.postDelayed(Runnable {
                        completedAdapter.notifyItemInserted(itemsCompleted.size - 1)
                    completedTextView.text = "Completed( "+itemsCompleted.size +" )"

                }, 700)

            }
        })

    }

    override fun unDoneItem(item: CallObject) {
        val index = itemsCompleted.indexOf(item)
        var handler = Handler()
        itemsCompleted.remove(item)

        handler.postDelayed({
            completedAdapter.notifyDataSetChanged()
            completedTextView.text = "Completed( "+itemsCompleted.size +" )"
        },500)


        var realm = Realm.getDefaultInstance()

        runOnUiThread({
            realm.executeTransaction({
                item.isDone = false
                items.add(item)
                handler.postDelayed({
                        pendingAdapter.notifyItemInserted(items.size - 1)
                }, 700)
            }
            )
        })
    }


    override fun editItem(id: Long) {
        val intent = Intent(this,AddNotes::class.java)
        intent.putExtra("do","edit")
        intent.putExtra("id",id)
        startActivity(intent)
    }



    override fun getContactName(number: String): String {
        return getContactName(number,this)
    }


    fun checkPermissions():Boolean {

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    100)

        }

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_STATE
                )
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
                    101)

        }
        return true
    }


    fun getContactName(phoneNumber: String, context: Context): String {

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            val uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber))
            val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)

            var contactName = ""
            val cursor = context.getContentResolver().query(uri, projection, null, null, null)

            if (cursor != null) {
                if (cursor!!.moveToFirst()) {
                    contactName = cursor!!.getString(0)
                }
                cursor!!.close()
            }

            return contactName


        }
        return ""
                }


    fun checkPermissionsUtility(){
        PermissionUtils.checkPermission(this,Manifest.permission.READ_PHONE_STATE,this)
        PermissionUtils.checkPermission(this,Manifest.permission.READ_CONTACTS,this)
    }

    override fun onNeedPermission(p:String) {
        ActivityCompat.requestPermissions(this,
                arrayOf(p),
                101)
    }

    override fun onPermissionPreviouslyDenied(p:String) {
        ActivityCompat.requestPermissions(this,
                arrayOf(p),
                101)
    }

    override fun onPermissionDisabled() {

    }

    override fun onPermissionGranted(p:String) {

    }

}
