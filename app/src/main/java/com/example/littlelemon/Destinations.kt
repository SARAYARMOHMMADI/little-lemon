package com.example.littlelemon

object Destinations {
    const val Onboarding = "onboarding"
    const val Home = "home"
    const val Profile = "profile"
    const val Cart = "cart"
    const val Track = "track"
    const val Detail = "detail/{itemId}"
    fun detailRoute(id: Int) = "detail/$id"

}