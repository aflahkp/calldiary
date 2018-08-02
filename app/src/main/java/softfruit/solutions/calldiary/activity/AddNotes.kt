package softfruit.solutions.calldiary.activity

import android.content.Intent
import android.icu.util.Calendar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import io.realm.Realm
import kotlinx.android.synthetic.main.dialog_add_notes.view.*
import softfruit.solutions.calldiary.R
import softfruit.solutions.calldiary.R.id.notesEditText
import softfruit.solutions.calldiary.R.id.saveButton
import softfruit.solutions.calldiary.R.layout.dialog_add_notes
import softfruit.solutions.calldiary.model.CallItem
import softfruit.solutions.calldiary.model.CallObject

class AddNotes : AppCompatActivity() {

    lateinit var number:String
    lateinit var note:String
    lateinit var noteEditText:EditText
    lateinit var titleEditText: EditText
    lateinit var showNotesImageButton: ImageButton
    lateinit var saveButton:Button
    var id:Long = 0
    var isEdit = false
    lateinit var callObject:CallObject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dialog_add_notes)

        Realm.init(this)

        noteEditText = findViewById<EditText>(notesEditText)
        titleEditText = findViewById<EditText>(R.id.titleEditText)
        showNotesImageButton = findViewById<ImageButton>(R.id.imageButton)
        saveButton = findViewById(R.id.saveButton)

        titleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                saveButton.isEnabled = !editable.isNullOrEmpty()
            }
        })

        showNotesImageButton.setOnClickListener(View.OnClickListener {
            val isVisible = noteEditText.visibility == View.VISIBLE
            if(isVisible){
                noteEditText.text = null
                noteEditText.visibility = View.GONE
                showNotesImageButton.isActivated = false
            }
            else{
                noteEditText.visibility = View.VISIBLE
                showNotesImageButton.isActivated = true
            }
        })

        if(intent.extras!=null){
            when(intent.extras.getString("do")) {
                "add"->{
                    number = intent.extras.getString("number")
                    id = Calendar.getInstance().timeInMillis
                    isEdit = false
                }
                "edit"->{
                    id = intent.extras.getLong("id")
                    isEdit = true
                    bindViews()
                }
            }

        }
    }

    fun save(view: View){
            if(!isEdit || callObject == null){
                callObject = CallObject()
                callObject.number = number
                callObject.isDone = false
                callObject.setId(id)
            }


            var realm = Realm.getDefaultInstance()


            realm.executeTransaction {
                callObject.notes = noteEditText.text.toString().trim()
                callObject.title = titleEditText.text.toString().trim()
                realm.insertOrUpdate(callObject)
            }

            Toast.makeText(this,"Note Added",Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if(!isEdit) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
                startActivity(intent)
            finish()
    }

    fun cancel(view: View){
        finish()
    }

    fun bindViews(){
        var realm = Realm.getDefaultInstance()
        callObject = realm.where(CallObject::class.java).equalTo("time",id).findFirst()!!

        titleEditText.setText(callObject.title)
        if(!callObject.notes.isNullOrEmpty()){
            noteEditText.visibility = View.VISIBLE
            showNotesImageButton.isActivated = true
        }
        noteEditText.setText(callObject.notes)
    }

}
