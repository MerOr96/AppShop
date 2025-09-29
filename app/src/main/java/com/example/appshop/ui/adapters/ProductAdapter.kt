package com.example.appshop.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appshop.R
import com.example.appshop.data.model.Product
import com.example.appshop.databinding.ItemProductBinding

class ProductAdapter(
    private val items: List<Product>,
    private val onClick: (Product) -> Unit,
    private val onFavorite: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.VH>() {

    inner class VH(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.tvTitle.text = product.title
            binding.tvPrice.text = binding.root.context.getString(R.string.product_price_format, product.price)
            Glide.with(binding.imgProduct.context).load(product.imageUrl).into(binding.imgProduct)
            binding.root.setOnClickListener { onClick(product) }
            binding.btnFavorite.setOnClickListener { onFavorite(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
