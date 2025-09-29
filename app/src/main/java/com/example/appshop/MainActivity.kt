package com.example.appshop

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appshop.data.model.Product
import com.example.appshop.data.repository.Repository
import com.example.appshop.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.appshop.ui.activities.CartActivity
import com.example.appshop.ui.activities.ItemActivity
import com.example.appshop.ui.adapters.ProductAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var repo: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repo = Repository(applicationContext)

        binding.recyclerProducts.layoutManager = LinearLayoutManager(this)

        binding.fabCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        // load data
        CoroutineScope(Dispatchers.IO).launch {
            var products = repo.getAllProducts()
            if (products.isEmpty()) {
                // Prepopulate (sample)
                val sample = listOf(
                    Product(
                        title = "Comic #1",
                        description = "Awesome comic #1",
                        price = 199.0,
                        imageUrl = "https://via.placeholder.com/300x200.png?text=Comic+1"
                    ),
                    Product(title = "Comic #2", description = "Cool comic #2", price = 249.0, imageUrl = "https://via.placeholder.com/300x200.png?text=Comic+2"),
                    Product(title = "Comic #3", description = "Vintage comic #3", price = 149.0, imageUrl = "https://via.placeholder.com/300x200.png?text=Comic+3")
                )
                repo.insertProducts(sample)
                products = repo.getAllProducts()
            }

            // switch to UI thread
            runOnUiThread {
                binding.recyclerProducts.adapter = ProductAdapter(
                    products,
                    onClick = { product: Product ->
                        val i = Intent(this@MainActivity, ItemActivity::class.java)
                        i.putExtra("product", product)
                        startActivity(i)
                    },
                    onFavorite = { product: Product ->
                        // TODO: implement favorites storage (simple toast for demo)
                    }
                )
            }
        }
    }
}
