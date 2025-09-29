package com.example.appshop.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.appshop.databinding.ItemCartBinding

class CartAdapter(
    private val items: MutableList<CartItem>,
    private val onQuantityChange: (CartItem) -> Unit,
    private val onRemove: (CartItem) -> Unit
) : RecyclerView.Adapter<CartAdapter.VH>() {

    inner class VH(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CartItem) {
            binding.tvCartTitle.text = item.title
            binding.tvCartPrice.text = "${item.price} ₽"
            binding.tvQuantity.text = item.quantity.toString()
            Glide.with(binding.imgCart.context).load(item.imageUrl).into(binding.imgCart)

            binding.btnPlus.setOnClickListener {
                item.quantity += 1
                binding.tvQuantity.text = item.quantity.toString()
                onQuantityChange(item)
            }
            binding.btnMinus.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity -= 1
                    binding.tvQuantity.text = item.quantity.toString()
                    onQuantityChange(item)
                }
            }
            binding.btnRemove.setOnClickListener {
                onRemove(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
