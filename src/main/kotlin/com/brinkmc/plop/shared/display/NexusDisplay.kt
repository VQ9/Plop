package com.brinkmc.plop.shared.display

import com.brinkmc.plop.Plop
import com.brinkmc.plop.plot.plot.base.Plot
import com.brinkmc.plop.shared.base.Addon
import com.brinkmc.plop.shared.base.State
import com.brinkmc.plop.shared.hooks.Display
import com.github.benmanes.caffeine.cache.Caffeine
import com.github.shynixn.mccoroutine.bukkit.launch
import com.github.shynixn.mccoroutine.bukkit.ticks
import com.sksamuel.aedile.core.asCache
import de.oliver.fancyholograms.api.data.TextHologramData
import de.oliver.fancyholograms.api.hologram.Hologram
import kotlinx.coroutines.delay
import org.bukkit.Location
import org.bukkit.entity.Player

class NexusDisplay(override val plugin: Plop): Addon, State {

    val active = Caffeine.newBuilder().asCache<Player, Location>()
    val playerHolograms = Caffeine.newBuilder().asCache<Player, Hologram>()
    val renderDelay = 3.ticks

    val hologramManager: Display
        get() = plugin.hooks.display

    override suspend fun load() {
        plugin.async { renderTask() }
    }

    override suspend fun kill() { }

    private suspend fun renderTask() {
        while (true) {
            for (player in server.onlinePlayers) {
                val loc = plugin.playerTracker.locations.get(player) {
                    player.getCurrentPlot()
                } ?: continue
                render(player, loc)
            }
            delay(renderDelay)
        }
    }

    private suspend fun render(player: Player, plot: Plot) { // Get centred starting location of hologram
        val startLoc = plot.nexus.getClosest(player.location)?.clone()?.add(0.5, 1.5, 0.5) ?: return // No nexus
        if (startLoc.distanceSquared(player.location) >= plotConfig.nexusConfig.viewDistance) { // Too far away
            far(player, startLoc)
        }
        else {
            near(player, plot, startLoc)
        }
    }

    private suspend fun far(player: Player, startLoc: Location) {
        if (!active.contains(player)) { // It was inactive to begin with
            return
        }

        active.invalidate(player)
        logger.info("Remove hologram")

        active[player] = startLoc // It was active, now it shouldn't be

        val potentialHologram = playerHolograms.getIfPresent(player) ?: return // Is there a hologram?
        hologramManager.hideHologram(player, potentialHologram) // Hide the hologram
    }

    private suspend fun near(player: Player, plot: Plot, location: Location) {
        if (active.getIfPresent(player) == location) { // It was active to begin with
            return
        }
        logger.info("Send hologram")

        active[player] = location // It was inactive, now it should be active as player is close

        val tags = lang.getTags(player = player, plot = plot) // Get tags and replace
        val substitutedValues = plotConfig.nexusConfig.display.map { lang.resolveTags(it, tags) }

        val potentialHologram = playerHolograms.get(player) {
            val hologramData = TextHologramData("nexus-${player.uniqueId}", location)
            hologramData.text = substitutedValues
            hologramManager.createHologram(hologramData)
        }
        // Teleport the hologram to the new location (should be the same text e.t.c)
        potentialHologram.data.setLocation(location)
        hologramManager.showHologram(player, potentialHologram)
    }
}