package app.ito.akki.praisesns

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sent_messages.*
import kotlinx.android.synthetic.main.mail.*

//自分の受信ボックスにメッセージが追加されたタイミングでメッセージがRecyclerViewに表示されるようにしていく必要がある。
class SentMessagesActivity : AppCompatActivity() {

    //lateinitで宣言することによってプロパティの初期化を遅らせることができる
    private lateinit var myEmailAddress: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DisplayMessageAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var db: FirebaseFirestore

    val postId: String = "id"
    var thanksButtonCount : Int = 0
    var goodButtonCount : Int = 0
    var workedHardCount : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sent_messages)

        myEmailAddress = FirebaseAuth.getInstance().currentUser?.email.toString()

        //受信ボックスのメッセージを取得してrecyclerViewに反映する
        db = FirebaseFirestore.getInstance()
        val allMessages = ArrayList<Post>()
//        val allMessages = mutableListOf<Post>()
        //名前を入力してコレクションを取得する
        db.collection("messages")
            //orderByを使用することでフィールドを指定し、データの並び替えができる
            //Query.Direction.DESCENDINGによって降順に並び替えることができる
            .orderBy("datetime", Query.Direction.DESCENDING)
            //以下でFirestoreの更新時の操作を登録
            //.addSnapshotListenerの中に書いた処理がデータベース更新時に自動で処理される
            //データの取得
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
                }
                //allMessagesから全ての要素を取り除く
                allMessages.clear()
                for (doc in value!!) {
                    val PostClass = doc.toObject<Post>()

                    allMessages.add(PostClass)

                    //Logに出力させることで値が保存されているか確認することができる
                    Log.d("sentMessages", "メッセージは「" + PostClass.message + "」")
                    Log.d("sender", "senderは「" + PostClass.sender + "」")
                }
//                allMessages.clear()
//                val post = value!!.first().toObject<Post>()
//                allMessages.add(post)

                //RecyclerViewの更新をする
                //RecyclerViewに紐づいているallMessagesの更新を表示に反映するために
                //notifyDataSetChanged()を使用する
                // iOSでいうtableView.reloadData()
                viewAdapter.myDataset = allMessages
                viewAdapter.notifyDataSetChanged()
            }
        //.get()
//            .addOnSuccessListener {result ->
//                for (document in result){
//                    val message = document.getString("message")
//                    val sender = document.getString("sender")
//                    //配列に要素を追加できるようにする
//                    allMessages.add(listOf(message, sender))
//                }
//            }

        //recyclerViewの設定
        viewManager = LinearLayoutManager(this)
        viewAdapter =
            DisplayMessageAdapter(allMessages).also { it.onThanksButtonClick = this::onClick }
                .also { it.onGoodButtonClick = this::onClick2 }.also { it.onWorkedHardButtonClick = this::onClick3 }
        recyclerView = messageInbox.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        post.setOnClickListener {
            val toMain = Intent(this, MainActivity::class.java)
            startActivity(toMain)
        }


        viewAdapter.setOnItemClickListener(object : DisplayMessageAdapter.OnItemClickListener {
            override fun onItemClickListener(
                view: View,
                postId: String
            ) {
                val context: Context = view.context
                val toReply = Intent(context, ReplyActivity::class.java)
                toReply.putExtra("key", postId)
                startActivity(toReply)
            }
        }
        )


    }

    fun onClick(view: View): Unit {

        thanksButtonCount = thanksButtonCount + 1
        db.collection("messages")
            .whereEqualTo("id", postId)
            .get()
            .addOnSuccessListener {
                val documentId = it.first().id
                val post = it.first().toObject<Post>()
                post.thanksButtonCount = thanksButtonCount.toString()
                db.collection("messages")
                    .document(documentId)
                    .set(post)
                    .addOnSuccessListener {
                        //データの保存が成功した際の処理
                        messageEdit.text.clear()
                    }
                    .addOnFailureListener { e ->
                        //データの保存が失敗した際の処理
                        //致命的なエラーが発生したらログに出力されるようにする。
                        Log.w("Firestore", "Error writing document", e)
                    }
            }
    }

    fun onClick2(view: View): Unit {
        goodButtonCount = goodButtonCount + 1
        db.collection("messages")
            .whereEqualTo("id", postId)
            .get()
            .addOnSuccessListener {
                val documentId = it.first().id
                val post = it.first().toObject<Post>()
                post.goodButtonCount = goodButtonCount.toString()
                db.collection("messages")
                    .document(documentId)
                    .set(post)
                    .addOnSuccessListener {
                        //データの保存が成功した際の処理
                        messageEdit.text.clear()
                    }
                    .addOnFailureListener { e ->
                        //データの保存が失敗した際の処理
                        //致命的なエラーが発生したらログに出力されるようにする。
                        Log.w("Firestore", "Error writing document", e)
                    }
            }
    }

    fun onClick3(view: View): Unit{
        workedHardCount = workedHardCount+1
        db.collection("messages")
            .whereEqualTo("id", postId)
            .get()
            .addOnSuccessListener {
                val documentId = it.first().id
                val post = it.first().toObject<Post>()
                post.workedHardButtonCount = workedHardCount.toString()
                db.collection("messages")
                    .document(documentId)
                    .set(post)
                    .addOnSuccessListener {
                        //データの保存が成功した際の処理
                        messageEdit.text.clear()
                    }
                    .addOnFailureListener { e ->
                        //データの保存が失敗した際の処理
                        //致命的なエラーが発生したらログに出力されるようにする。
                        Log.w("Firestore", "Error writing document", e)
                    }
            }
    }


}