package app.ito.akki.praisesns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_make_groups.*
import kotlin.random.Random

class MakeGroupsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    val NumberList = "0123456789"
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_groups)

        confirmName.setOnClickListener {
            createPassword()
            makeGroupID(groupName.text.toString(), password)
        }

        toDisplayGroups.setOnClickListener {
            //グループ一覧に移動できるようにする
            val toChooseGroups = Intent(this, ChooseGroupsActivity::class.java)
            startActivity(toChooseGroups)
        }
    }

    fun makeGroupID(Name: String, number: String){
        db = FirebaseFirestore.getInstance()

        val makeGroups = Groups(groupName = Name, password = number)
        db.collection("groups")
            .add(makeGroups)
            .addOnSuccessListener {
                titleTextView.text = "IDが発行されました。"
                groupID.text = number
            }
            .addOnFailureListener { e ->
                //致命的なエラーが発生したらLogに出力されるようにする
                Log.w("Firestore", "Error writing document", e)
            }
    }

    fun createPassword(){
        //８桁パスワードの作成
        for (i in 0..7){
            var randomIndex = Random.nextInt(NumberList.length)
            password += NumberList[randomIndex]
        }
    }
}