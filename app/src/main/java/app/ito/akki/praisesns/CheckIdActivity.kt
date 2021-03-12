package app.ito.akki.praisesns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_check_id.*

class CheckIdActivity : AppCompatActivity() {

    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_id)

        val sentId = intent.getStringExtra("key")
        button.setOnClickListener {
            db = FirebaseFirestore.getInstance()
            db.collection("group")
                .whereEqualTo("id",sentId)
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w("Firestore", "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    val groupId = value!!.first().toObject<Groups>()
                    if(groupId.groupID == checkIdEditText.text.toString()){
                        val toSentMessages = Intent(this, SentMessagesActivity::class.java)
                        startActivity(toSentMessages)
                    }
                }
        }
    }
}