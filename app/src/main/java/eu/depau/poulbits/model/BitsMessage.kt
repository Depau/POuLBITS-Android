package eu.depau.poulbits.model

import java.util.*

data class BitsMessage(
    val user: String,
    val message: String,
    val lastModified: Date
)