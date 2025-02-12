package com.brinkmc.plop.shared.gui.preview

import com.brinkmc.plop.Plop
import com.brinkmc.plop.plot.plot.base.PlotType
import com.brinkmc.plop.shared.base.Addon
import com.brinkmc.plop.shared.util.GuiUtils.description
import com.brinkmc.plop.shared.util.GuiUtils.name
import com.brinkmc.plop.shared.util.RegistrableInterface
import com.github.shynixn.mccoroutine.bukkit.launch
import com.noxcrew.interfaces.drawable.Drawable.Companion.drawable
import com.noxcrew.interfaces.element.StaticElement
import com.noxcrew.interfaces.interfaces.Interface
import com.noxcrew.interfaces.interfaces.buildPlayerInterface
import com.noxcrew.interfaces.properties.interfaceProperty
import kotlinx.coroutines.launch
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class HotbarPreview(override val plugin: Plop): Addon, RegistrableInterface {

    val BACK_BUTTON: ItemStack = ItemStack(Material.ARROW)
        .name(lang.get("preview.back-button.name"))
        .description(lang.get("preview.back-button.desc"))

    val FORWARD_BUTTON: ItemStack = ItemStack(Material.ARROW)
        .name(lang.get("preview.forward-button.name"))
        .description(lang.get("preview.forward-button.desc"))

    val CONFIRM_BUTTON: ItemStack = ItemStack(Material.GREEN_CONCRETE)
        .name(lang.get("preview.confirm-button.name"))
        .description(lang.get("preview.confirm-button.desc"))

    val TOGGLE_BUTTON_GUILD: ItemStack = ItemStack(Material.SHIELD)
        .name(lang.get("preview.toggle-button.guild-name"))
        .description(lang.get("preview.toggle-button.guild-desc"))

    val TOGGLE_BUTTON_PERSONAL: ItemStack = ItemStack(Material.TORCHFLOWER)
        .name(lang.get("preview.toggle-button.personal-name"))
        .description(lang.get("preview.toggle-button.personal-desc"))

    override suspend fun create(): Interface<*, *> = buildPlayerInterface {

        onlyCancelItemInteraction = false
        prioritiseBlockInteractions = false

        val plotType = interfaceProperty(PlotType.PERSONAL)
        var type by plotType

        withTransform(plotType) { pane, view ->

            type = plots.previewHandler.getPreview(view.player.uniqueId)!!.type

            pane.hotbar[0] = StaticElement(drawable(BACK_BUTTON)) { (player) -> plugin.async {
                plots.previewHandler.nextPlot(player.uniqueId)
            }}

            when (type) { // Determine the toggle button orientation
                PlotType.PERSONAL -> {
                    pane.hotbar[3] = StaticElement(drawable(TOGGLE_BUTTON_PERSONAL)) { (player) -> plugin.async {
                        plots.previewHandler.switchPreview(player.uniqueId) // Update preview
                    }
                }}
                PlotType.GUILD -> {
                    pane.hotbar[3] = StaticElement(drawable(TOGGLE_BUTTON_GUILD)) { (player) -> plugin.async {
                        plots.previewHandler.switchPreview(player.uniqueId) // Update preview
                    }
                }}
            }

            pane.hotbar[5] = StaticElement(drawable(CONFIRM_BUTTON)){ (player) -> plugin.async {
                plots.claimHandler.initiateClaim(player.uniqueId, type)
            }}
            pane.hotbar[8] = StaticElement(drawable(FORWARD_BUTTON)) { (player) -> plugin.async {
                plots.previewHandler.nextPlot(player.uniqueId)
            }}
        }
    }
}
