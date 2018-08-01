package softfruit.solutions.calldiary.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
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

import softfruit.solutions.calldiary.R
import softfruit.solutions.calldiary.adapter.CallListAdapter
import softfruit.solutions.calldiary.callback.DoneCallback
import softfruit.solutions.calldiary.model.CallObject

class MainActivity : AppCompatActivity(),DoneCallback {

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
}
