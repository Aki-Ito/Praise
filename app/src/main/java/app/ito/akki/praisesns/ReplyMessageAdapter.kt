package app.ito.akki.praisesns

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.mail.view.*

//ReplyMessageAdapterでリプを管理する
class ReplyMessageAdapter
//コンストラクタを追加
//コンストラクタって何？？
//クラスを作った時にすぐ代入されるもの
    (var myDataset: MutableList<Reply>)
//DisplayMessageAdapterクラスにRecyclerView.Adapterを継承する。
    : RecyclerView.Adapter<ReplyMessageAdapter.ViewHolder>(){


    //複数のViewを保持するクラスのこと
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sender: TextView = view.sender
        val message: TextView = view.message
    }

    // iOS: CellForRowAtのセルの作成部分（セルの作成）
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context)
            .inflate(R.layout.replymessage, parent, false)
        return ViewHolder(inflateView)
    }

    //リストの要素数を返すメソッドを実装する。
    // iOS: numberOfRowsInSection
    override fun getItemCount() = myDataset.size

    // iOS: CellForRowAt（データを設定）
    //myDatasetのposition番目の要素をrecyclerViewのviewに表示するコードを書く
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.message.text = myDataset[position].message
        holder.sender.text = "from: " + myDataset[position].sender

    }


}