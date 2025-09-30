package com.example.appshop.ui.activities

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.appshop.R
import com.example.appshop.data.model.CartItem
import com.example.appshop.data.model.Product
import com.example.appshop.data.repository.Repository
import com.example.appshop.databinding.ActivityItemBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemBinding
    private lateinit var repo: Repository
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = Repository(applicationContext)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        product = extractProduct()
        product?.let { p ->
            binding.tvDetailTitle.text = p.title
            binding.tvDetailPrice.text = getString(R.string.product_price_format, p.price)
            binding.tvDetailDesc.text = p.description
            Glide.with(this).load(p.imageUrl).into(binding.imgDetail)
        } ?: run {
            finish()
            return
        }

        binding.btnAddToCart.setOnClickListener {
            product?.let { p ->
                lifecycleScope.launch {
                    val item = CartItem(
                        productId = p.id,
                        title = p.title,
                        price = p.price,
                        quantity = 1,
                        imageUrl = p.imageUrl
                    )
                    withContext(Dispatchers.IO) {
                        repo.addToCart(item)
                    }
                    binding.btnAddToCart.setText(R.string.added_to_cart)
                }
            }
        }
    }

    private fun extractProduct(): Product? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("product", Product::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("product") as? Product
        }
    }
}
