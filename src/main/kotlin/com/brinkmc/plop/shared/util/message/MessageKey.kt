package com.brinkmc.plop.shared.util.message

enum class MessageKey(val key: String) {

    // Plot messages
    NOT_PLOT("plot.no_plot"),
    NOT_OWNER("plot.not-owner"),
    NOT_VISITABLE("plot.not_visitable"),


    // Shop messages
    PLAYER_INSUFFICIENT_BALANCE("shop.purchase.player.insufficient.balance"),
    SHOP_INSUFFICIENT_BALANCE("shop.purchase.insufficient.balance"),
    PLAYER_INSUFFICIENT_STOCK("shop.purchase.player.insufficient.stock"),
    SHOP_INSUFFICIENT_STOCK("shop.purchase.insufficient.stock"),
    SHOP_BUY_LIMIT_REACHED("shop.purchase.insufficient.buy_limit"),

    SHOP_PURCHASE_SUCCESSFUL("shop.purchase.successful"),

    // Teleport
    TELEPORT_IN_PROGRESS("plot.teleport.progress"),
    TELEPORT_INTERRUPTED("plot.teleport.interrupted"),
    TELEPORT_FAILED("plot.teleport.failed"),
    TELEPORT_COMPLETE("plot.teleport.complete"),



    // GUIs
    MENU_BUY_TITLE("gui.buy_player.title"),


    // General UI Items
    MENU_ERROR_NAME("gui.error.name"),
    MENU_ERROR_DESC("gui.error.desc"),

    MENU_ZERO_AMOUNT_NAME("gui.amount.zero.name"),
    MENU_ZERO_AMOUNT_DESC("gui.amount.zero.desc"),
    MENU_BAD_AMOUNT_NAME("gui.amount.bad.name"),
    MENU_BAD_AMOUNT_DESC("gui.amount.bad.desc"),

    MENU_PLAYER_INSUFFICIENT_BALANCE_NAME("gui.amount.player.insufficient.name"),
    MENU_PLAYER_INSUFFICIENT_BALANCE_DESC("gui.amount.player.insufficient.desc"),
    MENU_SHOP_INSUFFICIENT_BALANCE_NAME("gui.amount.shop.insufficient.name"),
    MENU_SHOP_INSUFFICIENT_BALANCE_DESC("gui.amount.shop.insufficient.desc"),
    MENU_PLAYER_INSUFFICIENT_STOCK_NAME("gui.amount.player.insufficient.name"),
    MENU_PLAYER_INSUFFICIENT_STOCK_DESC("gui.amount.player.insufficient.desc"),
    MENU_SHOP_INSUFFICIENT_STOCK_NAME("gui.amount.shop.insufficient.name"),
    MENU_SHOP_INSUFFICIENT_STOCK_DESC("gui.amount.shop.insufficient.desc"),

    MENU_BUY_LIMIT_REACHED_NAME("gui.amount.buy_limit.name"),
    MENU_BUY_LIMIT_REACHED_DESC("gui.amount.buy_limit.desc"),

    MENU_CONFIRM_BUY_NAME("gui.confirm.buy.name"),
    MENU_CONFIRM_BUY_DESC("gui.confirm.buy.desc"),

    MENU_MORE_NAME("gui.more.name"),
    MENU_MORE_DESC("gui.more.desc"),
    MENU_LESS_NAME("gui.less.name"),
    MENU_LESS_DESC("gui.less.desc"),
}