package com.example.store.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.store.Currency
import com.example.store.MainActivity
import com.example.store.Product
import com.example.store.R

class ProductsAdapter(private val activity: MainActivity, private val fragment: ProductsFragment) :
    RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder>() {
    var products = mutableListOf<Product>()
    var currency: Currency? = null

    inner class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var mProductName = itemView.findViewById<View>(R.id.productName) as TextView
        internal var mProductPrice = itemView.findViewById<View>(R.id.productPrice) as TextView
        internal var mAddToBasketButton =
            itemView.findViewById<View>(R.id.addToBasketButton) as Button
        internal var mProductImage = itemView.findViewById<View>(R.id.productImage) as ImageView


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
        )

    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        val current = products[position]
        Glide.with(activity)
            .load(current.image)
            .transition((DrawableTransitionOptions.withCrossFade()))
            .centerCrop()
            .override(600, 600)
            .into(holder.mProductImage)
        holder.mProductName.text = current.name

        val price = if (currency?.exchangeRate == null) current.price
        else current.price * currency?.exchangeRate!!
        holder.mProductPrice.text = activity.resources.getString(
            R.string.product_price,
            currency?.symbol,
            String.format("%2f", price)
        )
        if (current.inCart) {
            holder.mAddToBasketButton.text =
                activity.resources.getString(R.string.remove_from_basket)
            holder.mAddToBasketButton.setBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    android.R.color.holo_red_dark
                )
            )
        } else {
            holder.mAddToBasketButton.text = activity.resources.getString(R.string.add_to_basket)
            holder.mAddToBasketButton.setBackgroundColor(
                ContextCompat.getColor(
                    activity,
                    android.R.color.holo_green_dark
                )
            )
        }
        holder.mAddToBasketButton.setOnClickListener {
            fragment.updateCart(position)
        }

    }

    override fun getItemCount(): Int = products.size
}