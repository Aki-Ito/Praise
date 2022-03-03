package app.ito.akki.praisesns

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //FirebaseAuthオブジェクトの共有インスタンスを取得
        auth = FirebaseAuth.getInstance()
        //FirebaseFirestoreの設定
        db = FirebaseFirestore.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragment = inflater.inflate(R.layout.fragment_login, container, false)
        val buttonSignup = fragment.findViewById<Button>(R.id.SignUpButton)
        val buttonLogin = fragment.findViewById<Button>(R.id.LoginButton)

        buttonSignup.setOnClickListener {
            val emailEditText = fragment.findViewById<TextInputEditText>(R.id.emailEdit)
            val emailText = emailEditText.text.toString()
            val passEditText = fragment.findViewById<TextInputEditText>(R.id.passEdit)
            val passText = passEditText.text.toString()
            val userNameEditText = fragment.findViewById<TextInputEditText>(R.id.userNameEdit)
            val userNameText = userNameEditText.text.toString()

            if (emailText.isNullOrEmpty() == false && passText.isNullOrEmpty() == false) {
                auth.createUserWithEmailAndPassword(emailText, passText)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                activity, "SignUp 成功",
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
                                        activity, "SignUp 成功",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("username", userInfo.userName)
                                }


                            val toChooseGroups = Intent(activity, ChooseGroupsActivity::class.java)
                            startActivity(toChooseGroups)

                        } else {
                            Toast.makeText(
                                activity, "SignUp 失敗",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }else{
                Toast.makeText(
                    activity, "SignUp 失敗",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        buttonLogin.setOnClickListener {
            val emailEditText = fragment.findViewById<TextInputEditText>(R.id.emailEdit)
            val emailText = emailEditText.text.toString()
            val passEditText = fragment.findViewById<TextInputEditText>(R.id.passEdit)
            val passWordText = passEditText.text.toString()

            if (emailText.isNullOrEmpty() == false && passWordText.isNullOrEmpty() == false) {
                auth.signInWithEmailAndPassword(emailText, passWordText)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                activity, "Login 成功",
                                Toast.LENGTH_SHORT
                            ).show()
                            val toChooseGroups = Intent(activity, ChooseGroupsActivity::class.java)
                            startActivity(toChooseGroups)
                        } else {
                            Toast.makeText(
                                activity, "Login 失敗",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }else{
                Toast.makeText(
                    activity, "Login 失敗",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return fragment
    }




}