package app.ito.akki.praisesns

import java.util.*
import kotlin.collections.ArrayList

data class Post(
    val datetime: Date = Date(),
    val sender: String = "",
    val message: String = "",
    val reply: ArrayList<Reply?>?
)

