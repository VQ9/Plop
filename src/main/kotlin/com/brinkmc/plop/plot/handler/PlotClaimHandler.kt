package com.brinkmc.plop.plot.handler

import com.brinkmc.plop.Plop
import com.brinkmc.plop.shared.base.Addon
import com.brinkmc.plop.shared.base.State
import java.util.UUID

class PlotClaimHandler(override val plugin: Plop): Addon, State {
    override fun load() {
        TODO("Not yet implemented")
    }

    override fun kill() {
        TODO("Not yet implemented")
    }

    fun initiateClaim(player: UUID) {
        val previewInstance = plots.plotPreviewHandler.previews[player]

    }
}