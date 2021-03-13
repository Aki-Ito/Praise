package app.ito.akki.praisesns

import com.google.firebase.firestore.DocumentId
import java.time.chrono.ChronoLocalDateTime
import java.util.*


data class Reply (
    @DocumentId
    var id: String = "",
    val datetime: Date = Date(),
    val sender: String = "",
    val message: String = ""
)


