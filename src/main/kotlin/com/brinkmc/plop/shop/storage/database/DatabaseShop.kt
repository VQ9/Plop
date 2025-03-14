package com.brinkmc.plop.shop.storage.database

import com.brinkmc.plop.Plop
import com.brinkmc.plop.plot.plot.base.PlotType
import com.brinkmc.plop.shared.base.Addon
import com.brinkmc.plop.shared.base.State
import com.brinkmc.plop.shared.util.Funcs.fullString
import com.brinkmc.plop.shared.util.Funcs.toLocation
import com.brinkmc.plop.shop.shop.Shop
import com.brinkmc.plop.shop.shop.ShopTransaction
import com.brinkmc.plop.shop.shop.ShopType
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.bukkit.Location
import org.bukkit.inventory.ItemStack
import java.util.*

class DatabaseShop(override val plugin: Plop): Addon, State {

    private val mutex = Mutex()

    override suspend fun load() {}

    override suspend fun kill() {}

    suspend fun load(shopId: UUID): Shop? = mutex.withLock {
        val shop = loadShopCore(shopId) ?: return null
        val transaction = loadTransactions(shopId).toMutableList()
        return shop.copy(_transaction = transaction)
    }

    private suspend fun loadShopCore(shopId: UUID): Shop? {
        val rs = DB.query("SELECT * FROM shops WHERE shop_id=?", shopId.toString()) ?: return null
        return if (rs.next()) { // rs is the query, there should only be one result
            Shop(
                shopId = shopId,
                plotId = UUID.fromString(rs.getString("plot_id")),
                plotType = PlotType.valueOf(rs.getString("plot_type")),
                _location = rs.getString("shop_location").toLocation() ?: Location(null, 0.0, 0.0, 0.0), // Temporary placeholder location till it is taken from another section
                _item = ItemStack.deserializeBytes(rs.getBytes("item")),
                _quantity = rs.getInt("quantity"),
                _sellPrice = rs.getFloat("sell_price"),
                _buyPrice = rs.getFloat("buy_price"), // Reading the MySQL
                _buyLimit = rs.getInt("buy_limit"),
                _open = rs.getBoolean("open"),
                _transaction = mutableListOf() // Create empty list of transactions, populate later
            )
        } else null // No shop was found
    }


    // Transaction logic
    suspend fun loadTransactions(shopId: UUID): List<ShopTransaction> {
        val transactions = mutableListOf<ShopTransaction>()
        val rs = DB.query("SELECT * FROM shops_log WHERE shop_id=? ORDER BY trans_timestamp DESC", shopId.toString())

        while (rs?.next() == true) {
            transactions.add(
                ShopTransaction(
                    playerId = UUID.fromString(rs.getString("player_id")),
                    amount = rs.getInt("amount"),
                    type = ShopType.valueOf(rs.getString("type")),
                    timestamp = rs.getTimestamp("trans_timestamp")
                )
            )
        }

        return transactions
    }

    suspend fun create(shop: Shop) = mutex.withLock {
        val id = shop.shopId.toString()
        DB.update(
            "INSERT INTO shops (shop_id, plot_id, shop_location, plot_type, item, quantity, sell_price, buy_price, buy_limit, open) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
            id,
            shop.plotId.toString(),
            shop.location.fullString(),
            shop.plotType.toString(),
            shop.item.serializeAsBytes(),
            shop.quantity,
            shop.sellPrice,
            shop.buyPrice,
            shop.buyLimit,
            shop.open
        )
    }

    suspend fun save(unsafe: Shop) = mutex.withLock {
        val shop = unsafe.getSnapshot()
        val id = shop.shopId.toString()
        // Update shops table
        DB.update(
            "INSERT INTO shops (shop_id, plot_id, shop_location, plot_type, item, quantity, sell_price, buy_price, buy_limit, open) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE plot_id = VALUES(plot_id), plot_type = VALUES(plot_type), " +
                    "item = VALUES(item), quantity = VALUES(quantity), sell_price = VALUES(sell_price), " +
                    "buy_price = VALUES(buy_price), buy_limit = VALUES(buy_limit), open = VALUES(open)",
            id,
            shop.plotId.toString(),
            shop.location.fullString(),
            shop.plotType.toString(),
            shop.item.serializeAsBytes(),
            shop.quantity,
            shop.sellPrice,
            shop.buyPrice,
            shop.buyLimit,
            shop.open
        )
    }

    suspend fun delete(shop: Shop) = mutex.withLock {
        val id = shop.shopId.toString()
        // Delete from shops_log first (respect foreign key constraints)
        DB.update("DELETE FROM shops_log WHERE shop_id=?", id)
        // Finally delete from shops table
        DB.update("DELETE FROM shops WHERE shop_id=?", id)
    }

    suspend fun recordTransaction(shopId: UUID, transaction: ShopTransaction) = mutex.withLock {
        DB.update(
            "INSERT INTO shops_log (shop_id, player_id, amount, type) VALUES (?, ?, ?, ?)",
            shopId.toString(),
            transaction.playerId.toString(),
            transaction.amount,
            transaction.type.toString()
        )
    }

    suspend fun getShopsByPlotId(plotId: UUID): List<UUID> = mutex.withLock {
        val shops = mutableListOf<UUID>()
        val rs = DB.query("SELECT shop_id FROM shops WHERE plot_id=?", plotId.toString())

        while (rs?.next() == true) {
            val shopId = UUID.fromString(rs.getString("shop_id"))
            shops.add(shopId)
        }

        return shops
    }
}

