package com.example.appshop.data.dao

import androidx.room.*
import com.example.appshop.data.model.CartItem
@Dao
interface CartDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CartItem): Long

    @Update
    suspend fun update(item: CartItem)

    @Delete
    suspend fun delete(item: CartItem)

    @Query("SELECT * FROM cart_items")
    suspend fun getAll(): List<CartItem>

    @Query("SELECT * FROM cart_items WHERE productId = :productId LIMIT 1")
    suspend fun findByProductId(productId: Int): CartItem?

    @Query("DELETE FROM cart_items")
    suspend fun clear()
}
