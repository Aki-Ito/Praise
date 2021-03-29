package app.ito.akki.praisesns

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

//ログイン機能の実装 Firebase Auth
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        auth = FirebaseAuth.getInstance()
        //FirebaseFirestoreの設定
        db = FirebaseFirestore.getInstance()


        val buttonSignup = findViewById<Button>(R.id.SignUpButton)
        val buttonLogin = findViewById<Button>(R.id.LoginButton)


        buttonSignup.setOnClickListener {
            val emailEditText = findViewById<TextInputEditText>(R.id.emailEdit)
            val emailText = emailEditText.text.toString()
            val passEditText = findViewById<TextInputEditText>(R.id.passEdit)
            val passText = passEditText.text.toString()
            val userNameEditText = findViewById<TextInputEditText>(R.id.userNameEdit)
            val userNameText = userNameEditText.text.toString()

            if (emailText.isNullOrEmpty() == false && passText.isNullOrEmpty() == false) {
                auth.createUserWithEmailAndPassword(emailText, passText)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext, "SignUp 成功",
                                Toast.LENGTH_SHORT
                            ).show()
                            //ユーザのプロフィールを取得
                            val userInfo = UserInformation(userName = userNameText)
                            var myUid = FirebaseAuth.getInstance().currentUser?.uid
                            db.collection("user")
                                .document(myUid!!)
                                .set(userInfo)
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        baseContext, "SignUp 成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("username", userInfo.userName)
                                }


                            val toChooseGroups = Intent(this, ChooseGroupsActivity::class.java)
                            startActivity(toChooseGroups)

                        } else {
                            Toast.makeText(
                                baseContext, "SignUp 失敗",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }else{
                Toast.makeText(
                    baseContext, "SignUp 失敗",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        buttonLogin.setOnClickListener {
            val emailEditText = findViewById<TextInputEditText>(R.id.emailEdit)
            val emailText = emailEditText.text.toString()
            val passEditText = findViewById<TextInputEditText>(R.id.passEdit)
            val passWordText = passEditText.text.toString()

            if (emailText.isNullOrEmpty() == false && passWordText.isNullOrEmpty() == false) {
                auth.signInWithEmailAndPassword(emailText, passWordText)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext, "Login 成功",
                                Toast.LENGTH_SHORT
                            ).show()
                            val toChooseGroups = Intent(this, ChooseGroupsActivity::class.java)
                            startActivity(toChooseGroups)
                        } else {
                            Toast.makeText(
                                baseContext, "Login 失敗",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }else{
                Toast.makeText(
                    baseContext, "Login 失敗",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}