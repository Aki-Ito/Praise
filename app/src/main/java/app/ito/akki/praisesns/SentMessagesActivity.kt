package app.ito.akki.praisesns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_sent_messages.*

//自分の受信ボックスにメッセージが追加されたタイミングで送信ボタンの下に設置してあるRecyclerViewにメッセージが追加されていくようにする
class SentMessagesActivity : AppCompatActivity() {

    //lateinitで宣言することによってプロパティの初期化を遅らせることができる
    private lateinit var myEmailAddress: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var db: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sent_messages)

        //受信ボックスのメッセージを取得してrecyclerViewに反映する
        db = FirebaseFirestore.getInstance()
        val allMessages = ArrayList<List<String?>>()
        //名前を入力してコレクションを取得する
        db.collection("messages")
            .document(myEmailAddress)
            .collection("inbox")
            .orderBy("datetime", Query.Direction.DESCENDING)
            //以下でFirestoreの更新時の操作を登録
            //.addSnapshotListenerの中に書いた処理がデータベース更新時に自動で処理される
            .addSnapshotListener{ value, e ->
                if (e != null){
                Log.w("Firestore", "Listen failed.", e)
                    return@addSnapshotListener
            }
                allMessages.clear()
                for (doc in value!!){
                    val message = doc.getString("message")
                    val sender = doc.getString("sender")
                    allMessages.add(listOf(message, sender))
                }

                //RecyclerViewの更新をする
                //RecyclerViewに紐づいているallMessagesの更新を表示に反映するために
                //notifyDataSetChanged()を使用する
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



    }
}