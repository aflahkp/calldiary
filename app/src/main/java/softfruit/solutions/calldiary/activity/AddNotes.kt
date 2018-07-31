package softfruit.solutions.calldiary.activity

import android.content.Intent
import android.icu.util.Calendar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.EditText
import android.widget.Toast
import io.realm.Realm
import softfruit.solutions.calldiary.R.id.notesEditText
import softfruit.solutions.calldiary.R.layout.dialog_add_notes
import softfruit.solutions.calldiary.model.CallItem
import softfruit.solutions.calldiary.model.CallObject

class AddNotes : AppCompatActivity() {

    lateinit var number:String
    lateinit var note:String
    lateinit var noteEditText:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dialog_add_notes)

        noteEditText = findViewById<EditText>(notesEditText)
        if(intent.extras!=null){
            number = intent.extras.getString("number")
        }
    }

    fun save(view: View){
        if (noteEditText.text.toString().trim()!="") {
            var callObject = CallObject()
            callObject.notes = noteEditText.text.toString().trim()
            callObject.number = number
            callObject.isDone = false
            callObject.setId(Calendar.getInstance().timeInMillis)


            Realm.init(this)
            var realm = Realm.getDefaultInstance()


            realm.executeTransaction {
                realm.insertOrUpdate(callObject)
            }

            Toast.makeText(this,"Note Added",Toast.LENGTH_SHORT).show()

            val intent= Intent(this,MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

    }

    fun cancel(view: View){
        finish()
    }
}
