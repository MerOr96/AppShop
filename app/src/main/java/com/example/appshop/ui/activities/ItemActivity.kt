package com.example.appshop.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.appshop.databinding.ActivityItemBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemBinding
    private lateinit var repo: Repository
    private var product: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repo = Repository(applicationContext)

        product = intent.getSerializableExtra("product") as? Product
        product?.let { p ->
            binding.tvDetailTitle.text = p.title
            binding.tvDetailPrice.text = "${p.price} ₽"
            binding.tvDetailDesc.text = p.description
            Glide.with(this).load(p.imageUrl).into(binding.imgDetail)
        }

        binding.btnAddToCart.setOnClickListener {
            product?.let { p ->
                CoroutineScope(Dispatchers.IO).launch {
                    val item = CartItem(productId = p.id, title = p.title, price = p.price, quantity = 1, imageUrl = p.imageUrl)
                    repo.addToCart(item)
                    runOnUiThread {
                        // simple feedback
                        binding.btnAddToCart.text = "Added"
                    }
                }
            }
        }
    }
}
