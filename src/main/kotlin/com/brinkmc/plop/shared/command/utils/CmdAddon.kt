package com.brinkmc.plop.shared.command.utils

import org.bukkit.command.CommandSender
import org.incendo.cloud.paper.util.sender.Source
import org.bukkit.entity.Player
import org.incendo.cloud.paper.util.sender.PlayerSource

internal interface CmdAddon {

    fun getPlayer(sender: Source): Player {
        return sender.source() as Player
    }

    fun getPlayer(sender: CommandSender): Player {
        return sender as Player
    }
}