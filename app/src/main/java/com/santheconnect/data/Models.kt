package com.santheconnect.data

data class Santhe(
    val id: String = "",
    val name: String = "",
    val day: String = "",
    val specialty: String = "",
    val type: String = "",
    val emoji: String = "",
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val distance: String = ""
)

data class Eatery(
    val id: Int = 0,
    val name: String = "",
    val item: String = "",
    val rating: Double = 0.0,
    val reviews: Int = 0,
    val open: Boolean = false,
    val tag: String = "",
    val emoji: String = ""
)

object SampleData {

    val DAYS = listOf(
        "Sunday",
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday"
    )

    val SANTHES = listOf<Santhe>()
}
