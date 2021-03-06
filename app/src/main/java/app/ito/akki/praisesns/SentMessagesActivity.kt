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
import kotlinx.android.synthetic.main.activity_sent_messages.*

//自分の受信ボックスにメッセージが追加されたタイミングでメッセージがRecyclerViewに表示されるようにしていく必要がある。
class SentMessagesActivity : AppCompatActivity() {

    //lateinitで宣言することによってプロパティの初期化を遅らせることができる
    private lateinit var myEmailAddress: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DisplayMessageAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var db: FirebaseFirestore

    val postId: String = "id"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sent_messages)

        myEmailAddress = FirebaseAuth.getInstance().currentUser?.email.toString()

        //受信ボックスのメッセージを取得してrecyclerViewに反映する
        db = FirebaseFirestore.getInstance()
        val allMessages = ArrayList<Post>()
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
                for (doc in value!!) {
                    val PostClass = doc.toObject<Post>()

                    allMessages.add(PostClass)

                    //Logに出力させることで値が保存されているか確認することができる
                    Log.d("sentMessages", "メッセージは「" + PostClass.message + "」")
                    Log.d("sender", "senderは「" + PostClass.sender + "」")
                }

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
        viewAdapter = DisplayMessageAdapter(allMessages)
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
                val toReply=Intent(context, ReplyActivity::class.java)
                toReply.putExtra("key",postId)
                startActivity(toReply)
//                context.startActivity(Intent(context, ReplyActivity::class.java))
            }
           }
        )


    }
}