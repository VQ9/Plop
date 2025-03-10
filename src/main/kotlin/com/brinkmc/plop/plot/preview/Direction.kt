package com.brinkmc.plop.plot.preview

import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.concurrent.atomic.AtomicBoolean

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun next(): Direction {
        return when (this) {
            NORTH -> EAST
            EAST -> SOUTH
            SOUTH -> WEST
            WEST -> NORTH
        }
    }
}

data class StringLocation(
    val world: String,
    val x: Double,
    val y: Double,
    val z: Double,
    private var _free: AtomicBoolean = AtomicBoolean(true)
) {
    var free: Boolean
        get() = _free.get()
        set(value) {
            _free.set(value)
        }

    fun toLocation(): Location {
        return Location(Bukkit.getWorld(world), x, y, z)
    }
}