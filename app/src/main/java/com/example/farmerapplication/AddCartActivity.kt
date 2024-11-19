package com.example.farmerapplication

import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddCartActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDatabaseHelper
    private lateinit var cartItems: List<CartItem>
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cart)

        dbHelper = UserDatabaseHelper(this)


        cartItems = dbHelper.getCartItems()

        val cartListView: ListView = findViewById(R.id.cartListView)
        cartAdapter = CartAdapter(this, cartItems)
        cartListView.adapter = cartAdapter

        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show()
        }
    }
}
