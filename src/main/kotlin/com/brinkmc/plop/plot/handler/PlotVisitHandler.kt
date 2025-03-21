package com.brinkmc.plop.plot.handler

import com.brinkmc.plop.Plop
import com.brinkmc.plop.plot.plot.base.Plot
import com.brinkmc.plop.plot.plot.base.PlotType
import com.brinkmc.plop.shared.base.Addon
import com.brinkmc.plop.shared.base.State
import com.brinkmc.plop.shared.config.serialisers.Level

class PlotVisitHandler(override val plugin: Plop): Addon, State {

    private val guildLevels = mutableListOf<Level>()
    private val personalLevels = mutableListOf<Level>()

    override suspend fun load() {
        plotConfig.getVisitorLimit(PlotType.GUILD).let { guildLevels.addAll(it) } // Add all guild plot size levels
        plotConfig.getVisitorLimit(PlotType.PERSONAL).let { personalLevels.addAll(it) }
    }

    override suspend fun kill() {
        guildLevels.clear()
        personalLevels.clear()
    }

    // Getters

    fun getHighestLevel(plotType: PlotType): Int {
       return when (plotType) {
           PlotType.GUILD -> guildLevels.size - 1
           PlotType.PERSONAL -> personalLevels.size - 1
       }
    }

    fun getLevel(plotType: PlotType, toFind: Int): Level {
        return when (plotType) {
            PlotType.GUILD -> guildLevels[toFind]
            PlotType.PERSONAL -> personalLevels[toFind]
        }
    }

    fun getCurrentVisitorLimit(plotType: PlotType, level: Int): Int {
        return when (plotType) {
            PlotType.GUILD -> guildLevels[level].value ?: -1
            PlotType.PERSONAL -> personalLevels[level].value ?: -1
        }
    }

    fun getMaximumVisitorLimit(plotType: PlotType): Int {
        return when (plotType) {
            PlotType.GUILD -> guildLevels.last().value ?: -1
            PlotType.PERSONAL -> personalLevels.last().value ?: -1
        }
    }

    // Setters

    fun upgradePlot(plot: Plot) {
        plot.visit.level += 1
    }
}