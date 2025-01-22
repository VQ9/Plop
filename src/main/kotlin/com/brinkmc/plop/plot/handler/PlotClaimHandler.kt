package com.brinkmc.plop.plot.handler

import com.brinkmc.plop.Plop
import com.brinkmc.plop.plot.plot.base.Plot
import com.brinkmc.plop.plot.plot.base.PlotType
import com.brinkmc.plop.plot.plot.data.PlotClaim
import com.brinkmc.plop.plot.plot.data.PlotVisitState
import com.brinkmc.plop.plot.plot.modifier.PlotFactory
import com.brinkmc.plop.plot.plot.modifier.PlotSize
import com.brinkmc.plop.plot.plot.modifier.PlotShop
import com.brinkmc.plop.plot.plot.modifier.PlotTotem
import com.brinkmc.plop.plot.plot.modifier.PlotVisitLimit
import com.brinkmc.plop.shared.base.Addon
import com.brinkmc.plop.shared.base.State
import com.brinkmc.plop.shared.util.LocationUtils.getSafeDestination
import com.brinkmc.plop.shared.util.async
import kotlinx.atomicfu.atomic
import java.sql.Timestamp
import java.util.UUID

class PlotClaimHandler(override val plugin: Plop): Addon, State {
    override suspend fun load() {
        TODO("Not yet implemented")
    }

    override suspend fun kill() {
        TODO("Not yet implemented")
    }

    suspend fun initiateClaim(player: UUID, plotType: PlotType) = async {
        val previewInstance = plots.plotPreviewHandler.previews[player] ?: return@async

        val plotClaim = PlotClaim(
            previewInstance.viewPlot.value.world,
            previewInstance.viewPlot.value.toLocation(),
            previewInstance.viewPlot.value.toLocation().getSafeDestination() ?: previewInstance.viewPlot.value.toLocation(),
            previewInstance.viewPlot.value.toLocation().getSafeDestination() ?: previewInstance.viewPlot.value.toLocation()
        )

        val newUUID = UUID.randomUUID()
        val newPlot = Plot(
            newUUID,
            plotType,
            player,
            plotClaim,
            PlotVisitState(true,mutableListOf<Timestamp>()),
            PlotVisitLimit(0, 0, plotType),
            PlotSize(0, plotType),
            PlotFactory(0, mutableListOf(), plotType),
            PlotShop(0, mutableListOf(), plotType),
            PlotTotem(0, mutableListOf(), plotType) // No totems for a brand-new plot
        )

        plots.plotHandler.addPlot(newPlot) // Register new plot in handler

        plugin.hooks.worldGuard.createRegion(newUUID)
        TODO("Integrate with Guilds, Integrate with WorldGuard")

        plots.plotPreviewHandler.claimPlot(player)
    }
}