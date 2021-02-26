package app.ito.akki.praisesns

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.mail.view.*

class DisplayMessageAdapter
    //コンストラクタを追加
    //コンストラクタって何？？
    //クラスを作った時にすぐ代入されるもの
    (private val myDataset: ArrayList<List<String?>>)
    //DisplayMessageAdapterクラスにRecyclerView.Adapterを継承する。
    : RecyclerView.Adapter<DisplayMessageAdapter.ViewHolder>(){

    //複数のViewを保持するクラスのこと
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sender: TextView = view.sender
        val message: TextView = view.message
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflateView = LayoutInflater.from(parent.context)
            .inflate(R.layout.mail, parent, false)
        return ViewHolder(inflateView)
    }

    //リストの要素数を返すメソッドを実装する。
    override fun getItemCount() = myDataset.size

    //myDatasetのposition番目の要素をrecyclerViewのviewに表示するコードを書く
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.message.text = myDataset[position][0]
        holder.sender.text = "from: " + myDataset[position][1]
    }
}