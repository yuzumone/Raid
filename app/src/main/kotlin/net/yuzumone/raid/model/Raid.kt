package net.yuzumone.raid.model

class Raid {
    var place: String? = ""
    val pokemon: String? = ""
    val date: String? = ""
    val player: Int = 0

    override fun toString(): String {
        return "Raid{place='$place', pokemon='$pokemon', date='$date', player='$player'}"
    }
}