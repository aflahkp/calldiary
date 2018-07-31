package softfruit.solutions.calldiary.activity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.LinearLayout
import io.realm.Realm
import kotlinx.android.synthetic.main.content_main.*

import softfruit.solutions.calldiary.R
import softfruit.solutions.calldiary.adapter.CallListAdapter
import softfruit.solutions.calldiary.model.CallItem
import softfruit.solutions.calldiary.model.CallObject

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView:RecyclerView
    lateinit var adapter:CallListAdapter
    lateinit var items:ArrayList<CallObject>
    lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        items = ArrayList()
        recyclerView = callerListRecyclerView
        adapter = CallListAdapter(items)
        layoutManager = LinearLayoutManager(this,LinearLayout.VERTICAL,false)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        loadItems()

    }

    fun loadItems(){
        Realm.init(this)
        var realm = Realm.getDefaultInstance()

        var realmObjects = realm.where(CallObject::class.java).findAll()
        items.addAll(realmObjects)
        adapter.notifyDataSetChanged()
    }

}
