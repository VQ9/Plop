package com.brinkmc.plop.plot.plot.modifier

import com.brinkmc.plop.plot.plot.base.PlotType
import java.util.UUID

data class PlotShop(
    var level: Int,
    @Transient internal val  plotType: PlotType
)