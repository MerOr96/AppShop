package com.example.appshop.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appshop.data.repository.Repository
import com.example.appshop.databinding.ActivityCartBinding
import com.example.appshop.ui.adapters.CartAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCartBinding
    private lateinit var repo: Repository
    private var adapter: CartAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = Repository(applicationContext)

        binding.recyclerCart.layoutManager = LinearLayoutManager(this)

        loadCart()

        binding.btnCheckout.setOnClickListener {
            // demo: clear cart
            CoroutineScope(Dispatchers.IO).launch {
                repo.clearCart()
                runOnUiThread {
                    loadCart()
                }
            }
        }
    }

    private fun loadCart() {
        CoroutineScope(Dispatchers.IO).launch {
            val items = repo.getCartItems()
            val total = items.sumOf { it.price * it.quantity }
            runOnUiThread {
                adapter = CartAdapter(items.toMutableList(),
                    onQuantityChange = { item ->
                        CoroutineScope(Dispatchers.IO).launch {
                            repo.updateCartItem(item)
                            loadCart()
                        }
                    },
                    onRemove = { item ->
                        CoroutineScope(Dispatchers.IO).launch {
                            repo.removeCartItem(item)
                            loadCart()
                        }
                    }
                )
                binding.recyclerCart.adapter = adapter
                binding.tvTotal.text = "Total: ${((total * 100).roundToInt() / 100.0)} ₽"
            }
        }
    }
}
