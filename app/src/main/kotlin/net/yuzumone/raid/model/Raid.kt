package net.yuzumone.raid.model

class Raid {
    var place: String? = ""
    var pokemon: String? = ""
    var time: String? = ""
    var player: Int = 0

    override fun toString(): String {
        return "Raid{place='$place', pokemon='$pokemon', time='$time', player='$player'}"
    }
}