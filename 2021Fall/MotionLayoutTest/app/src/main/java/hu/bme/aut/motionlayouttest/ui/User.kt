package hu.bme.aut.motionlayouttest.ui

import java.util.*

data class User(
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val photo: String
)