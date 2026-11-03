package com.example.brewkery.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.brewkery.data.local.entity.ActiveOrderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query("SELECT * FROM active_orders ORDER BY placedAtTimestamp DESC LIMIT 1")
    fun getLatestOrder(): Flow<ActiveOrderEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrder(order: ActiveOrderEntity)

    @Query("DELETE FROM active_orders")
    suspend fun clearOrders()
}
