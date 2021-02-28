package app.ito.akki.praisesns

import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

//ログイン機能の実装 Firebase Auth
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        auth = FirebaseAuth.getInstance()


        val buttonSignup = findViewById<Button>(R.id.SignUpButton)
        val buttonLogin = findViewById<Button>(R.id.LoginButton)


        buttonSignup.setOnClickListener {
            val emailEditText = findViewById<EditText>(R.id.emailEditText)
            val emailText = emailEditText.text.toString()
            val passEditText = findViewById<EditText>(R.id.passEditText)
            val passWordText = passEditText.text.toString()

            //メールアドレスとパスワードを使い、新規登録をする
            //新規登録する際にはcreateUserWithEmailAndPasswordを用いる
            auth.createUserWithEmailAndPassword(emailText, passWordText)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "SignUp 成功",
                            Toast.LENGTH_SHORT
                        ).show()
                        val toMainActivity = Intent(this, MainActivity::class.java)
                        startActivity(toMainActivity)

                    } else {
                        Toast.makeText(
                            baseContext, "SignUp 失敗",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

        buttonLogin.setOnClickListener {
            val emailEditText = findViewById<EditText>(R.id.emailEditText)
            val emailText = emailEditText.text.toString()
            val passEditText = findViewById<EditText>(R.id.passEditText)
            val passWordText = passEditText.text.toString()

            auth.signInWithEmailAndPassword(emailText, passWordText)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            baseContext, "Login 成功",
                            Toast.LENGTH_SHORT
                        ).show()
                        val toMainActivity = Intent(this, MainActivity::class.java)
                        startActivity(toMainActivity)
                    } else {
                        Toast.makeText(
                            baseContext, "Login 失敗",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }

    }
}