package com.brinkmc.plop.shared.command.plot.general

import com.brinkmc.plop.Plop
import com.brinkmc.plop.plot.plot.base.PlotType
import com.brinkmc.plop.shared.base.Addon
import com.brinkmc.plop.shared.util.cmd.CmdAddon
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command

class CommandPlotVisit(override val plugin: Plop) : Addon, CmdAddon {

    @Command("plot visit|visit <Player> [PlotType]")
    suspend fun visit(
        sender: CommandSourceStack,
        @Argument("Player") receiver: Player,
        @Argument("PlotType") type: PlotType?
    ) {
        val player = getPlayer(sender.sender)

        val choice = plugin.menus.selectionOtherMenu.requestChoice(player, receiver, type, null)

        if (choice == null) {
            player.sendMiniMessage("plot.no-plot")
            return
        }

        postTypeChosen(player, receiver, choice)
    }

    suspend fun postTypeChosen(player: Player, receiver: Player, type: PlotType) {
        val plot = receiver.getPlot(type)

        if (plot?.visit?.visitable == false) {
            player.sendMiniMessage("plot.not-visitable")
            return
        }

        plot?.claim?.visit?.let { player.teleportAsync(it) }
        player.sendMiniMessage("plot.teleport-complete")
    }
}