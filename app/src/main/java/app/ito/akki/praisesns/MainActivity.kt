package app.ito.akki.praisesns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import androidx.appcompat.widget.Toolbar;

class MainActivity : AppCompatActivity() {
    //lateinitで宣言することによってプロパティの初期化を遅らせる
    private lateinit var myEmailAddress: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val mainToolbar = findViewById<Toolbar>(R.id.toolbar)
//        setSupportActionBar(mainToolbar)
//        //ツールバーに戻るボタンを設置する
//        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        // アクションバーにツールバーをセット
        setSupportActionBar(toolbar)
        // ツールバーに戻るボタンを設置
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val groupSentId = intent.getStringExtra("ID2")

        //自分のメールアドレスが取得できるようにする
        myEmailAddress = FirebaseAuth.getInstance().currentUser?.email.toString()

        //自分のメールアドレスが表示されるようにする
        myId.setText(myEmailAddress)


        //送信ボタンを押した時の設定
        send.setOnClickListener {
            if (groupSentId != null) {
                sendMessage(messageEdit.text.toString(),groupSentId)
            }
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

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        val menuInflater = menuInflater
//        menuInflater.inflate(R.menu.menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.email -> {
//                val toSentMessagesActivity = Intent(this, SentMessagesActivity::class.java)
//                startActivity(toSentMessagesActivity)
//                true
//            }
//            R.id.logout -> {
//                finish()
//                true
//            }
//
//            else -> super.onOptionsItemSelected(item)
//        }
//    }


    //メッセージをデータベースに格納する
    fun sendMessage(message: String, groupSentId: String) {
        val db = FirebaseFirestore.getInstance()


        //map...keyとvalueを一つのセットにしてデータを管理する
        //他言語ではハッシュや辞書(ディクショナリ)と呼ばれるもの
        //Firestoreでデータを登録する際、Hashを必ず使用する。型が指定されている。


        val mail2 = Post( sender = myEmailAddress, message = message)

        //collectionにいれたものがコレクションに入る

        db.collection("groups")
            .document(groupSentId)
            .collection("messages")
            //add()メソッドを用いると勝手に一意なIDがドキュメント名に対して作成される
            //追加する
            .add(mail2)
            .addOnSuccessListener {
                //データの保存が成功した際の処理
                messageEdit.text.clear()
            }
            .addOnFailureListener { e ->
                //データの保存が失敗した際の処理
                //致命的なエラーが発生したらログに出力されるようにする。
                Log.e("Firestore", "Error writing document", e)
            }
    }
}

