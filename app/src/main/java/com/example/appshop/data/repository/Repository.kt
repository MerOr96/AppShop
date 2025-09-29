package com.example.appshop.data.repository

import android.content.Context
import com.example.appshop.data.db.AppDatabase
import com.example.appshop.data.model.CartItem
import com.example.appshop.data.model.Product

class Repository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val productDao = db.productDao()
    private val cartDao = db.cartDao()

    suspend fun getAllProducts() = productDao.getAll()
    suspend fun getProduct(id: Int) = productDao.getById(id)
    suspend fun insertProducts(items: List<Product>) = productDao.insertAll(items)

    suspend fun getCartItems() = cartDao.getAll()
    suspend fun addToCart(item: CartItem) {
        val existing = cartDao.findByProductId(item.productId)
        if (existing != null) {
            existing.quantity += item.quantity
            cartDao.update(existing)
        } else {
            cartDao.insert(item)
        }
    }
    suspend fun updateCartItem(item: CartItem) = cartDao.update(item)
    suspend fun removeCartItem(item: CartItem) = cartDao.delete(item)
    suspend fun clearCart() = cartDao.clear()
}
