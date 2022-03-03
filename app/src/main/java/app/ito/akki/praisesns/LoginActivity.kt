package app.ito.akki.praisesns

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//ログイン機能の実装 Firebase Auth
class LoginActivity : AppCompatActivity() , AgreementFragment.OnAgreeFragmentListener{

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

       auth = FirebaseAuth.getInstance()
//        if (auth.currentUser == null){
            //FragmentTransactionのインスタンスを取得する
            val agreementFragment = AgreementFragment()
            val transaction = supportFragmentManager.beginTransaction()
            //addして追加する。一つ目の引数には親であるViewGroup、二つ目の引数には追加するフラグメント
            transaction.add(R.id.container, agreementFragment)
            //コミットしないと変更が適用されない。
            transaction.commit()
//        }

    }

    override fun onAgreeFragment(){
        val loginFragment = LoginFragment()
        val transaction = supportFragmentManager.beginTransaction()
        //idで識別されるコンテナに現在あるフラグメントをfugaFragmentに置き換える。
        transaction.replace(R.id.container, loginFragment)
        //バックスタックにトランザクションを追加する。戻るボタンを選択すると前のフラグメントに戻れるようにする。
        transaction.addToBackStack(null)
        transaction.commit()
    }
}