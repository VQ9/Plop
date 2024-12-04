package com.brinkmc.plop.plot.plot

import com.brinkmc.plop.plot.plot.base.Plot
import com.brinkmc.plop.plot.plot.data.Claim
import com.brinkmc.plop.plot.plot.data.PlotVisit
import com.brinkmc.plop.plot.plot.modifier.FactoryLimit
import com.brinkmc.plop.plot.plot.modifier.PlotSize
import com.brinkmc.plop.plot.plot.modifier.ShopLimit
import com.brinkmc.plop.plot.plot.modifier.VisitorLimit
import com.brinkmc.plop.plot.plot.structure.Totem
import java.util.*


/*
I find best practice for data classes tends to be separate the logic into extension functions.
This works well in keeping the data class itself relatively clean
 */

data class PersonalPlot (
    // State
    override val plotId: UUID, // Unique ID for the plot

    // Primary
    val ownerId: UUID, // May or may not be a guild plot?

    override val claim: Claim,

    override val totems: MutableList<Totem>,
    override val plotVisit: PlotVisit,

    // Mutable
    override var plotSize: PlotSize,
    override var factoryLimit: FactoryLimit,
    override var shopLimit: ShopLimit,
    override var visitorLimit: VisitorLimit

): Plot(plotId, claim, totems, visits, plotSize, factoryLimit, shopLimit, visitorLimit) {

    fun addMember() {
        //TODO
    }

    fun removeMember() {
        //TODO
    }
}

