package app.ito.akki.praisesns

import java.time.chrono.ChronoLocalDateTime
import java.util.*


data class Reply (
    val datetime: Date = Date(),
    val sender: String = "",
    val message: String = ""
)


