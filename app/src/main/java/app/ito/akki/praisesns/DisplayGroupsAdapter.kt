package app.ito.akki.praisesns

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.groups.view.*

//ReplyMessageAdapterでリプを管理する
class DisplayGroupsAdapter
//コンストラクタを追加
//コンストラクタって何？？
//クラスを作った時にすぐ代入されるもの
    (var myDataset: MutableList<Groups>)
//DisplayMessageAdapterクラスにRecyclerView.Adapterを継承する。
    : RecyclerView.Adapter<DisplayGroupsAdapter.ViewHolder>(){

    //リスナを格納する変数を定義する(lateinitで初期化を遅らせている)
    lateinit var listener: OnItemClickListener

    //複数のViewを保持するクラスのこと
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.groupNameTextView
        val datetime: TextView = view.dateTextView
        val container: CardView = view.cardView
    }

    // iOS: CellForRowAtのセルの作成部分（セルの作成）
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context)
            .inflate(R.layout.groups, parent, false)
        return ViewHolder(inflateView)
    }

    //リストの要素数を返すメソッドを実装する。
    // iOS: numberOfRowsInSection
    override fun getItemCount() = myDataset.size

    // iOS: CellForRowAt（データを設定）
    //myDatasetのposition番目の要素をrecyclerViewのviewに表示するコードを書く
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val dateToString: String = myDataset[position].datetime.toString()
        holder.datetime.text = dateToString
        holder.name.text = myDataset[position].groupName
        holder.container.setOnClickListener{
            listener.onItemClickListener(it, myDataset[position])
        }

    }


    //インタフェースを作成する
    interface OnItemClickListener {
        fun onItemClickListener(view: View, group: Groups)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }


}