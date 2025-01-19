package com.brinkmc.plop.plot.handler

import com.brinkmc.plop.Plop
import com.brinkmc.plop.plot.layout.GuildPlotLayoutStrategy
import com.brinkmc.plop.plot.layout.PersonalPlotLayoutStrategy
import com.brinkmc.plop.plot.plot.base.plotType
import com.brinkmc.plop.plot.preview.PreviewInstance
import com.brinkmc.plop.shared.base.Addon
import com.brinkmc.plop.shared.base.State
import com.brinkmc.plop.shared.util.GuiUtils.stacksToBase64
import org.bukkit.Bukkit
import java.util.UUID

/*
Keep track of all active preview instances
Ensure that data is saved e.t.c
 */

class PlotPreviewHandler(override val plugin: Plop): Addon, State {

    lateinit var guildPlotLayoutStrategy: GuildPlotLayoutStrategy
    lateinit var personalPreviewHandler: PersonalPlotLayoutStrategy

    val previews = mutableMapOf<UUID, PreviewInstance>()

    override suspend fun load() {
        guildPlotLayoutStrategy = GuildPlotLayoutStrategy(plugin)
        personalPreviewHandler = PersonalPlotLayoutStrategy(plugin)
    }

    override suspend fun kill() {
        previews.clear()
    }

    fun startPreview(player: UUID, type: plotType) {

        val bukkitPlayer = Bukkit.getPlayer(player)

        if (bukkitPlayer == null) { // Validation check to see if player exists
            logger.error("Failed to start preview, player doesn't exist")
            return
        }

        val previewInstance = PreviewInstance( // Create new preview instance
            plugin,
            player,
            bukkitPlayer.location.clone(),
            stacksToBase64(bukkitPlayer.inventory.contents)
        )

        // Always start on a personal plot preview
        previewInstance.type = type

        when (type) {
            plotType.PERSONAL -> {
                previewInstance.viewPlot = personalPreviewHandler.getFirstFree() ?: run {
                    logger.error("No free personal plots :(") // Handle having no free plots
                    return
                }
            }

            plotType.GUILD -> {
                previewInstance.viewPlot = guildPlotLayoutStrategy.getFirstFree() ?: run {
                    logger.error("No free guild plots :(") // Handle having no free plots
                    return
                }
            }
        }

        previewInstance.viewPlot.value.open = false

        previewInstance.setHotbarInventory() // Update player inventory

        // Add to the loaded instances
        previews.put(player, previewInstance)
        return
    }

    fun endPreview(player: UUID) {
        val previewInstance = previews[player] // Get preview

        if (previewInstance == null) {
            logger.error("No such preview")
            return
        }

        previewInstance.viewPlot.value.open = true

        previewInstance.returnInventory()
        previewInstance.returnTeleport()

        previews.remove(player) // Remove from map
        return
    }

    fun claimPlot(player: UUID) {
        val previewInstance = previews[player] // Get preview

        if (previewInstance == null) {
            logger.error("No such preview")
            return
        }

        when(previewInstance.type) {
            plotType.PERSONAL -> {
                personalPreviewHandler.openPlots.remove(previewInstance.viewPlot)
            }
            plotType.GUILD -> {
                guildPlotLayoutStrategy.openPlots.remove(previewInstance.viewPlot)
            }
        }

        previewInstance.returnInventory() // Return inventory

        previews.remove(player) // Remove from map
    }

    fun nextPlot(player: UUID) {
        val previewInstance = previews[player]

        if (previewInstance == null) {
            logger.error("No such preview")
            return
        }

        previewInstance.viewPlot.value.open = true
        when (previewInstance.type) { // Handle guild vs personal logic
            plotType.PERSONAL ->  {
                previewInstance.viewPlot = personalPreviewHandler.getNextFreePlot(previewInstance.viewPlot) ?: run {
                    logger.error("No free personal plots forwards :(") // Handle having no free plots
                    return
                }
            }
            plotType.GUILD -> {
                previewInstance.viewPlot = guildPlotLayoutStrategy.getNextFreePlot(previewInstance.viewPlot) ?: run {
                    logger.error("No free guild plots forwards :(") // Handle having no free plots
                    return
                }
            }
        }
        previewInstance.viewPlot.value.open = false

        previewInstance.teleportToViewPlot() // Update player logic
        return
    }

    fun previousPlot(player: UUID) {
        val previewInstance = previews[player]

        if (previewInstance == null) {
            logger.error("No such preview")
            return
        }

        previewInstance.viewPlot.value.open = true
        when (previewInstance.type) { // Handle guild vs personal logic
            plotType.PERSONAL ->  {
                previewInstance.viewPlot = personalPreviewHandler.getPreviousFreePlot(previewInstance.viewPlot) ?: run {
                    logger.error("No free personal plots backwards :(") // Handle having no free plots
                    return
                }
            }
            plotType.GUILD -> {
                previewInstance.viewPlot = guildPlotLayoutStrategy.getPreviousFreePlot(previewInstance.viewPlot) ?: run {
                    logger.error("No free guild plots backwards :(") // Handle having no free plots
                    return
                }
            }
        }
        previewInstance.viewPlot.value.open = false

        previewInstance.teleportToViewPlot() // Update player logic
        return
    }
}