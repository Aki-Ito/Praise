package app.ito.akki.praisesns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_join_groups.*

class JoinGroupsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_groups)


        //editTextでenterが押されたら乱数が作成
        groupName.setOnEditorActionListener{ view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                //完了(DONE)だったらやりたい処理

                makeGroupID()

            }
            return@setOnEditorActionListener true
        }

        button.setOnClickListener {
            //IDの入力画面に遷移する
        }
    }

    fun makeGroupID(){
        var num = (0..99999999).shuffled().last()
        groupID.text = num.toString()
        db.collection("group")
            .add(num)
            .addOnSuccessListener {
                titleTextView.text = "IDが発行されました。"
            }
            .addOnFailureListener { e ->
                //致命的なエラーが発生したらLogに出力されるようにする
                Log.w("Firestore", "Error writing document", e)
            }


    }
}