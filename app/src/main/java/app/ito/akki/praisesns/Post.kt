package app.ito.akki.praisesns

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

data class Post(
    @DocumentId
    var id: String = "",
    val datetime: Date = Date(),
    val sender: String = "",
    val message: String = "",
    var thanksButtonCount: Int = 0,
    var goodButtonCount: Int = 0,
    var workedHardButtonCount: Int = 0
)



