package com.brinkmc.plop.shared.command.plot.nexus

import com.brinkmc.plop.Plop
import com.brinkmc.plop.shared.base.Addon
import com.brinkmc.plop.shared.util.cmd.CmdAddon
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.incendo.cloud.annotations.Command

internal class CommandPlotSetHome(override val plugin: Plop) : Addon, CmdAddon {

    @Command("plot set home")
    suspend fun setHome(
        sender: CommandSourceStack
    ) {
        val player = getPlayer(sender.sender)
        val plot = player.getCurrentPlot()

        if (plot == null) {
            player.sendMiniMessage("plot.not-in-plot")
            return
        }

        if (!plot.owner.isPlayer(player)) {
            player.sendMiniMessage("plot.not-owner")
            return
        }

        if (!player.hasPermission("plop.plot.command.sethome")) {
            player.sendMiniMessage("plot.no-permission")
            return
        }

        plot.claim.home = player.location
        player.sendMiniMessage("plot.home-set")
    }
}


