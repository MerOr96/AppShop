package com.example.appshop.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appshop.R
import com.example.appshop.data.model.CartItem
import com.example.appshop.data.repository.Repository
import com.example.appshop.databinding.ActivityCartBinding
import com.example.appshop.ui.adapters.CartAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var repo: Repository
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = Repository(applicationContext)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        adapter = CartAdapter(mutableListOf(),
            onQuantityChange = { item ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        repo.updateCartItem(item)
                    }
                    loadCart()
                }
            },
            onRemove = { item ->
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        repo.removeCartItem(item)
                    }
                    loadCart()
                }
            }
        )

        binding.recyclerCart.layoutManager = LinearLayoutManager(this)
        binding.recyclerCart.adapter = adapter

        lifecycleScope.launch {
            loadCart()
        }

        binding.btnCheckout.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    repo.clearCart()
                }
                loadCart()
            }
        }
    }

    private suspend fun loadCart() {
        val items = withContext(Dispatchers.IO) { repo.getCartItems() }
        adapter.updateItems(items)
        updateTotal(items)
    }

    private fun updateTotal(items: List<CartItem>) {
        val total = items.sumOf { it.price * it.quantity }
        binding.tvTotal.text = getString(R.string.cart_total_format, total)
        binding.btnCheckout.isEnabled = items.isNotEmpty()
    }
}
