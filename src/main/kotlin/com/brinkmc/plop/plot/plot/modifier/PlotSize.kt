package com.brinkmc.plop.plot.plot.modifier

import com.brinkmc.plop.plot.plot.base.PlotType

data class PlotSize(
    var level: Int,
    @Transient internal val plotType: PlotType
)