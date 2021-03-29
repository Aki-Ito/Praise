package app.ito.akki.praisesns

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toolbar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_check_id.*
import kotlinx.android.synthetic.main.activity_choose_groups.*


class ChooseGroupsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: DisplayGroupsAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_groups)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        // アクションバーにツールバーをセット
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)





        auth = FirebaseAuth.getInstance()

        //受信ボックスのメッセージを取得してrecyclerViewに反映する
        db = FirebaseFirestore.getInstance()
        var allMessages = mutableListOf<Groups>()
        //名前を入力してコレクションを取得する
        db.collection("groups")
            //orderByを使用することでフィールドを指定し、データの並び替えができる
            //Query.Direction.DESCENDINGによって降順に並び替えることができる
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
                    val GroupClass = doc.toObject<Groups>()

                    allMessages.add(GroupClass)
                }

                Log.d("allmessages", allMessages.toString())


                //RecyclerViewの更新をする
                //RecyclerViewに紐づいているallMessagesの更新を表示に反映するために
                //notifyDataSetChanged()を使用する
                // iOSでいうtableView.reloadData()
                viewAdapter.myDataset = allMessages
                viewAdapter.notifyDataSetChanged()
            }


        //recyclerViewの設定
        viewManager = GridLayoutManager(this, 4, GridLayoutManager.VERTICAL, false)
        viewAdapter =
            DisplayGroupsAdapter(allMessages)
        recyclerView = groupRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        groupMaking.setOnClickListener {
            val toMaking = Intent(this, MakeGroupsActivity::class.java)
            startActivity(toMaking)
        }

        viewAdapter.setOnItemClickListener(object : DisplayGroupsAdapter.OnItemClickListener {
            override fun onItemClickListener(
                view: View,
                group: Groups
            ) {
                val context: Context = view.context
//                val toCheck = Intent(context, CheckIdActivity::class.java)
//                toCheck.putExtra("key", group.documentID)
//                startActivity(toCheck)

                val myedit = EditText(this@ChooseGroupsActivity)
                val dialog = AlertDialog.Builder(this@ChooseGroupsActivity)
                dialog.setTitle("ルームIDを入力してください")
                dialog.setView(myedit)
                dialog.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->
                    // OKボタン押したときの処理
                    val userText = myedit.getText().toString()
                    db = FirebaseFirestore.getInstance()
                    db.collection("groups")
                        .document(group.documentID)
                        .addSnapshotListener { value, e ->
                            if (e != null) {
                                Log.w("Firestore", "Listen failed.", e)
                                return@addSnapshotListener
                            }
                            val group = value!!.toObject<Groups>()
                            if(group!!.password == userText){
                                val toSentMessages = Intent(context, SentMessagesActivity::class.java)
                                toSentMessages.putExtra("ID", group.documentID)
                                startActivity(toSentMessages)

                            }
                        }
                })
                dialog.setNegativeButton("キャンセル", null)
                dialog.show()

            }
        }
        )
    }


    //ツールバーのメニューを初期化する
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.logout -> {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



}