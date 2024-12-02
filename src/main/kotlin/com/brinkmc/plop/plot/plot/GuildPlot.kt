package com.brinkmc.plop.plot.plot

import com.brinkmc.plop.plot.plot.modifier.FactoryLimit
import com.brinkmc.plop.plot.plot.modifier.PlotSize
import com.brinkmc.plop.plot.plot.modifier.ShopLimit
import com.brinkmc.plop.plot.plot.modifier.VisitorLimit
import com.brinkmc.plop.plot.plot.structure.Totem
import org.bukkit.Bukkit
import org.incendo.cloud.paper.PaperCommandManager
import java.util.*


/*
I find best practice for data classes tends to be separate the logic into extension functions.
This works well in keeping the data class itself relatively clean
 */

data class GuildPlot (
    // State
    override val plotId: UUID, // Unique ID for the plot

    // Primary
    val guildId: UUID, // May or may not be a guild plot?

    val members: MutableList<UUID>?,
    override val claim: Claim,

    override val totems: MutableList<Totem>,
    override var visits: PlotVisits,

    // Mutable
    override var plotSize: PlotSize,
    override var factoryLimit: FactoryLimit,
    override var shopLimit: ShopLimit,
    override var visitorLimit: VisitorLimit

): Plot(guildId, claim, totems, visits, plotSize, factoryLimit, shopLimit, visitorLimit) {

    fun getLeader() {

    }

    fun addMember() {
        //TODO
    }

    fun removeMember() {
        //TODO
    }
}

