package app.ito.akki.praisesns

import com.google.firebase.firestore.DocumentId
import java.util.*

data class Groups (
    @DocumentId
    var documentID: String = "",
    val datetime: Date = Date(),
    var groupName: String = "",
    var password: String = "00000000"
)