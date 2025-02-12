package com.brinkmc.plop.plot.layout

import com.brinkmc.plop.Plop
import com.brinkmc.plop.plot.plot.base.Plot
import com.brinkmc.plop.plot.plot.base.PlotType
import com.brinkmc.plop.plot.preview.Direction
import com.brinkmc.plop.plot.preview.StringLocation
import com.brinkmc.plop.shared.base.Addon
import com.brinkmc.plop.shared.base.State
import com.brinkmc.plop.shared.config.configs.PlotConfig
import com.brinkmc.plop.shared.util.collection.LinkedList
import com.brinkmc.plop.shared.util.collection.Node
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.WorldCreator
import kotlin.math.max
import kotlin.times

class GuildPlotLayoutStrategy(override val plugin: Plop): BaseLayoutStrategy(plugin) {

    override val maxPlotLength: Double = plotConfig.getPlotMaxSize(PlotType.GUILD)?.toDouble() ?: run {
        logger.error("Failed to get max plot length for guild plots")
        0.0
    }
    override val maxPreviewLimit: Int
        get() = (plugin.plots.handler.getPlotMap().size * 3) + 100
    override val worldName: String = plotConfig.getPlotWorld(PlotType.GUILD) ?: run {
        logger.error("Failed to get world name for guild plots")
        ""
    }
    override val worldGen: String = plotConfig.getPlotWorldGenerator(PlotType.GUILD) ?: run {
        logger.error("Failed to get world generator for guild plots")
        ""
    }

}