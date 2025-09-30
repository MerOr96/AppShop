package com.example.appshop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appshop.R
import com.example.appshop.data.model.Product
import com.example.appshop.data.repository.Repository
import com.example.appshop.databinding.ActivityMainBinding
import com.example.appshop.ui.activities.CartActivity
import com.example.appshop.ui.activities.ItemActivity
import com.example.appshop.ui.adapters.ProductAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repo: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = Repository(applicationContext)

        setSupportActionBar(binding.toolbar)
        binding.bottomNavigation.selectedItemId = R.id.menu_home
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    false
                }
                else -> true
            }
        }

        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)

        binding.fabCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        loadProducts()
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.selectedItemId = R.id.menu_home
    }

    private fun loadProducts() {
        lifecycleScope.launch(Dispatchers.IO) {
            var products = repo.getAllProducts()
            if (products.isEmpty()) {
                val sample = listOf(
                    Product(
                        title = "Comic #1",
                        description = "Awesome comic #1",
                        price = 199.0,
                        imageUrl = "https://via.placeholder.com/300x200.png?text=Comic+1"
                    ),
                    Product(
                        title = "Comic #2",
                        description = "Cool comic #2",
                        price = 249.0,
                        imageUrl = "https://via.placeholder.com/300x200.png?text=Comic+2"
                    ),
                    Product(
                        title = "Comic #3",
                        description = "Vintage comic #3",
                        price = 149.0,
                        imageUrl = "https://via.placeholder.com/300x200.png?text=Comic+3"
                    )
                )
                repo.insertProducts(sample)
                products = repo.getAllProducts()
            }

            withContext(Dispatchers.Main) {
                binding.recyclerProducts.adapter = ProductAdapter(
                    products,
                    onClick = { product: Product ->
                        val intent = Intent(this@MainActivity, ItemActivity::class.java)
                        intent.putExtra("product", product)
                        startActivity(intent)
                    },
                    onFavorite = { _ ->
                        // TODO: implement favorites storage (e.g. Room table or SharedPreferences)
                    }
                )
            }
        }
    }
}
