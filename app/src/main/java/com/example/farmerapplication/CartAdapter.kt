package com.example.farmerapplication


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.BaseAdapter

class CartAdapter(private val context: Context, private val cartItems: List<CartItem>) : BaseAdapter() {

    override fun getCount(): Int {
        return cartItems.size
    }

    override fun getItem(position: Int): Any {
        return cartItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.cart_item_layout, parent, false)

        val productNameTextView: TextView = view.findViewById(R.id.cartItemName)
        val productPriceTextView: TextView = view.findViewById(R.id.cartItemPrice)
        val productQuantityTextView: TextView = view.findViewById(R.id.cartItemQuantity)

        val cartItem = cartItems[position]

        productNameTextView.text = cartItem.productName
        productPriceTextView.text = cartItem.productPrice
        productQuantityTextView.text = "Quantity: ${cartItem.quantity}"

        return view
    }
}
