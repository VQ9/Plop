package com.brinkmc.plop.plot.layout

import com.brinkmc.plop.Plop
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

class PersonalPlotLayoutStrategy(override val plugin: Plop, override val plotType: PlotType): BaseLayoutStrategy(plugin, plotType) {

    override val maxPlotLength: Double = plotConfig.getPlotMaxSize(plotType).toDouble()
    override val maxPreviewLimit: Int
        get() = (plugin.hooks.worldGuard.getPlotRegions(plotType).size * 3) + 100
    override val worldName: String = plotConfig.getPlotWorld(plotType)
    override val worldGen: String = plotConfig.getPlotWorldGenerator(plotType)

}