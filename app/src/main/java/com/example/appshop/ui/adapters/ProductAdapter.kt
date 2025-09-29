package com.example.appshop.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(
    private val items: List<Product>,
    private val onClick: (Product) -> Unit,
    private val onFavorite: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.VH>() {

    inner class VH(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(p: Product) {
            binding.tvTitle.text = p.title
            binding.tvPrice.text = "${p.price} ₽"
            Glide.with(binding.imgProduct.context).load(p.imageUrl).into(binding.imgProduct)
            binding.root.setOnClickListener { onClick(p) }
            binding.btnFavorite.setOnClickListener { onFavorite(p) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}