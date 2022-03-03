package app.ito.akki.praisesns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_make_groups.*
import java.util.*
import kotlin.random.Random

class MakeGroupsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    val NumberList = "0123456789"
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_groups)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // アクションバーにツールバーをセット
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        confirmName.setOnClickListener {
            createPassword()
            Log.d("password", password)
//            makeGroupID(groupName.text.toString(), password)
            makeGroupID(groupName.text.toString(),password)
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
        //4桁パスワードの作成
        for (i in 0..3){
            var randomIndex = Random.nextInt(NumberList.length)
            password += NumberList[randomIndex]
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // android.R.id.home に戻るボタンを押した時のidが取得できる
        if (item.itemId == android.R.id.home) {
            // 今回はActivityを終了させている
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


}