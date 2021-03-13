package app.ito.akki.praisesns

import com.google.firebase.firestore.DocumentId
import java.util.*

data class Groups (
    @DocumentId
    var documentID: String = "",
    var groupName: String = "",
    var password: String = "00000000"
)