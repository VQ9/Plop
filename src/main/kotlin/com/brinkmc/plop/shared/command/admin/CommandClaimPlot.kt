package com.brinkmc.plop.shared.command.admin

import com.brinkmc.plop.Plop
import com.brinkmc.plop.plot.plot.Plot
import com.brinkmc.plop.shared.base.Addon
import org.bukkit.entity.Player
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command

internal class CommandClaimPlot(override val plugin: Plop) : Addon {

    @Command("admin claim plot")
    suspend fun claimPlot(
        player: Player
    ) {
        if (!player.hasPermission("plop.admin.claim")) {

            return
        }
    }

}


