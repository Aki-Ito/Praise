package app.ito.akki.praisesns

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.mail.view.*

class ReplyMessageAdapter
//コンストラクタを追加
//コンストラクタって何？？
//クラスを作った時にすぐ代入されるもの
    (var myDataset: ArrayList<List<String?>>)
//DisplayMessageAdapterクラスにRecyclerView.Adapterを継承する。
    : RecyclerView.Adapter<ReplyMessageAdapter.ViewHolder>(){

    //リスナを格納する変数を定義する(lateinitで初期化を遅らせている)
    lateinit var listener: OnItemClickListener

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
        holder.message.text = myDataset[position][0]
        holder.sender.text = "from: " + myDataset[position][1]

    }

    //インタフェースを作成する
    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: List<String?>)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }
}