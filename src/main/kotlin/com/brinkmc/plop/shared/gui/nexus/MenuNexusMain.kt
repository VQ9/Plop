package com.brinkmc.plop.shared.gui.nexus

import com.brinkmc.plop.Plop
import com.brinkmc.plop.shared.base.Addon
import com.noxcrew.interfaces.drawable.Drawable.Companion.drawable
import com.noxcrew.interfaces.element.StaticElement
import com.noxcrew.interfaces.interfaces.buildChestInterface
import com.noxcrew.interfaces.view.InterfaceView
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class MenuNexusMain(override val plugin: Plop): Addon {

    val MAIN_OVERVIEW
        get() = ItemStack(Material.PLAYER_HEAD)
        .name("nexus.main.overview.name")

    val UPGRADES
        get() = ItemStack(Material.SPLASH_POTION)
        .name("nexus.main.upgrades.name")

    val TOTEMS
        get() = ItemStack(Material.LIGHTNING_ROD)
        .name("nexus.main.totems.name")

    private val inventory = buildChestInterface {
        onlyCancelItemInteraction = false
        prioritiseBlockInteractions = false
        rows = 6

        withTransform { pane, view ->
            // Determine plot
            val plot = view.player.getCurrentPlot() ?: return@withTransform

            // Main overview button
            val individualMainOverview = MAIN_OVERVIEW
                .setSkull(plot.owner)
                .description("nexus.main.overview.description",
                    player = view.player,
                    plot = plot
                )

            pane[2, 4] = StaticElement(drawable(individualMainOverview)) { (player) -> plugin.async {
                open(player)
            } }

            // Upgrades button
            pane[2, 2] = StaticElement(drawable(UPGRADES)) { (player) -> plugin.async {
                plugin.menus.nexusUpgradeMenu.open(player, view)
            } }

            // Totems button
            pane[2, 6] = StaticElement(drawable(TOTEMS)) { (player) -> plugin.async {
                plugin.menus.nexusTotemsMenu.open(player, view)
            } }
        }
    }

    suspend fun open(player: Player, prev: InterfaceView? = null) {
        inventory.open(player, prev)
    }
}