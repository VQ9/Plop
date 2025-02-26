package com.brinkmc.plop.shared.util.message.tags

import com.brinkmc.plop.Plop
import com.brinkmc.plop.plot.plot.base.Plot
import com.brinkmc.plop.shared.base.Addon
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.entity.Player

class PlotTags(override val plugin: Plop, val miniMessage: MiniMessage) : Addon {

    fun all(plot: Plot): TagResolver {

        return TagResolver.resolver(
            name(plot),
            shopLevel(plot)
        )

    }

    private fun name(plot: Plot): TagResolver {
        return Placeholder.component(
            "plot_owner",
            miniMessage.deserialize(plot.owner.getName())
        )
    }

    private fun shopLevel(plot: Plot): TagResolver {
        return Placeholder.component(
            "plot_shop_level",
            Component.text(plot.shop.level)
        )
    }
}
