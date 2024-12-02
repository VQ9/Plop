package com.brinkmc.plop.shared.util

import com.brinkmc.plop.Plop
import com.brinkmc.plop.shared.base.Addon
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentBuilder
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.Tag
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.entity.Player

class MessageService(override val plugin: Plop): Addon {

    private val profileTags = ProfileTags(plugin)
    private lateinit var miniMessage: MiniMessage

    val plopMessageSource = plugin.plopMessageSource()

    fun init() {
        miniMessage = MiniMessage.builder()
            .tags(TagResolver.builder() // Define some of the tags
                .resolver(StandardTags.defaults())
                .build()
            )
            .build()
    }

    fun notAdmin(): String {
        return plopMessageSource.findMessage("plop.notadmin") ?: "<red>No message set!"
    }

    // Provide functionality to the Addon reference
    override fun Player.sendFormattedMessage(message: String) {

        if (player == null) {
            logger.error("Failed to send message!")
            return
        }

        player?.sendMessage(
            miniMessage.deserialize(message, profileTags.name(player))
        )
    }
}

class ProfileTags(override val plugin: Plop) : Addon {

    fun all(player: Player?): TagResolver {

        return TagResolver.resolver(
            name(player)
            plot(player)
            shop(player)
        )

    }

    fun name(player: Player?): TagResolver {
        return Placeholder.component("name", player!!.displayName())
    }

    fun plot(player: Player?): TagResolver {
        with (plots) {
            player?.personalPlot()
        }

    }

    fun shop(player: Player?): TagResolver {
        with (shops) {
            player?.shops()
        }
    }

}



