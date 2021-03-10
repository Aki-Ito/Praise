package app.ito.akki.praisesns

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

data class Post(
    var id: String = UUID.randomUUID().toString(),
    val datetime: Date = Date(),
    val sender: String = "",
    val message: String = "",
    val reply: MutableList<Reply> = mutableListOf(),
    var thanksButtonCount: Int = 0,
    var goodButtonCount: Int = 0,
    var workedHardButtonCount: Int = 0
)



