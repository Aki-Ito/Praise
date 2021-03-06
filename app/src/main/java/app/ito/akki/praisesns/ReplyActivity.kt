package app.ito.akki.praisesns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.messageEdit
import kotlinx.android.synthetic.main.activity_main.send
import kotlinx.android.synthetic.main.activity_reply.*
import kotlinx.android.synthetic.main.activity_sent_messages.*
import java.text.SimpleDateFormat
import java.util.*


class ReplyActivity : AppCompatActivity() {

    private lateinit var myEmailAddress: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ReplyMessageAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reply)

        myEmailAddress = FirebaseAuth.getInstance().currentUser?.email.toString()

        send.setOnClickListener {
            replyMessage(
                messageEdit.text.toString()
            )
        }

        myEmailAddress = FirebaseAuth.getInstance().currentUser?.email.toString()

        //受信ボックスのメッセージを取得してrecyclerViewに反映する
        db = FirebaseFirestore.getInstance()
        val allMessages = ArrayList<List<String?>>()
        //名前を入力してコレクションを取得する
        db.collection("reply")
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
                for (doc in value!!) {
                    val ReplyClass = doc.toObject<Reply>()

                    allMessages.add(listOf(ReplyClass.message, ReplyClass.sender))

                    //Logに出力させることで値が保存されているか確認することができる
                    Log.d("sentMessages", "メッセージは「" + ReplyClass.message + "」")
                    Log.d("sender", "senderは「" + ReplyClass.sender + "」")
                }

                //RecyclerViewの更新をする
                //RecyclerViewに紐づいているallMessagesの更新を表示に反映するために
                //notifyDataSetChanged()を使用する
                // iOSでいうtableView.reloadData()
                viewAdapter.myDataset = allMessages
                viewAdapter.notifyDataSetChanged()
            }


        viewManager = LinearLayoutManager(this)
        viewAdapter = ReplyMessageAdapter(allMessages)
        recyclerView = replyMessageInbox.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    //メッセージをデータベースに格納する
    fun replyMessage(message: String) {
        val db = FirebaseFirestore.getInstance()

        //現在時刻の取得
        val date = Date()

        //map...keyとvalueを一つのセットにしてデータを管理する
        //他言語ではハッシュや辞書(ディクショナリ)と呼ばれるもの
        //Firestoreでデータを登録する際、Hashを必ず使用する。型が指定されている。
//        val mail = hashMapOf(
//            "datetime" to format.format(date),
//            "sender" to myEmailAddress,
//            "message" to message
//        )

        val mail2 = Reply(date, myEmailAddress, message)



        //collectionにいれたものがコレクションに入る
        db.collection("reply") //usersとかmail

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
                Log.w("Firestore", "Error writing document", e)
            }
    }
}