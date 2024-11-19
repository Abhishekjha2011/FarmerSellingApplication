package com.example.farmerapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class CartListActivity : AppCompatActivity() {

    private lateinit var dbHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        dbHelper = UserDatabaseHelper(this)

        val productPriceTextView: TextView = findViewById(R.id.cartProductPrice)
        val productImageView1: ImageView = findViewById(R.id.cartProductImage1)
        val productImageView2: ImageView = findViewById(R.id.cartProductImage2)
        val productImageView3: ImageView = findViewById(R.id.cartProductImage3)
        val productImageView4: ImageView = findViewById(R.id.cartProductImage4)
        val productImageView5: ImageView = findViewById(R.id.cartProductImage5)
        val productImageView6: ImageView = findViewById(R.id.cartProductImage6)
        val productImageView7: ImageView = findViewById(R.id.cartProductImage7)
        val productImageView8: ImageView = findViewById(R.id.cartProductImage8)


        val totalPrice = intent.getStringExtra("totalPrice") ?: "₹ 0"
        val selectedProductNames = intent.getStringArrayListExtra("selectedProductNames") ?: ArrayList()
        val selectedProductImages = intent.getIntegerArrayListExtra("selectedProductImages") ?: ArrayList()
        val selectedProductQuantities = intent.getIntegerArrayListExtra("selectedProductQuantities") ?: ArrayList()


        productPriceTextView.text = totalPrice


        val imageViews = listOf(
            productImageView1, productImageView2, productImageView3, productImageView4,
            productImageView5, productImageView6, productImageView7, productImageView8
        )

        for (i in selectedProductImages.indices) {
            if (i < imageViews.size) {
                imageViews[i].setImageResource(selectedProductImages[i])
                imageViews[i].visibility = ImageView.VISIBLE
            }
        }

        for (i in selectedProductImages.size until imageViews.size) {
            imageViews[i].visibility = ImageView.GONE
        }

        findViewById<Button>(R.id.buyNowButton).setOnClickListener {

            val intent = Intent(this, DeliveryActivity::class.java)
            intent.putExtra("PRODUCT_NAME", selectedProductNames.joinToString(", ")) // Adjust to fetch dynamically
            intent.putExtra("PRODUCT_PRICE", totalPrice)
            intent.putIntegerArrayListExtra("SELECTED_PRODUCT_IMAGES", selectedProductImages)
            startActivity(intent)
        }

        findViewById<Button>(R.id.addToCartButton).setOnClickListener {

            val productName = selectedProductNames.joinToString(", ")
            val productPrice = totalPrice
            val quantity = selectedProductQuantities.sum()

            val isInserted = dbHelper.addProductToCart(productName, productPrice, quantity)

            if (isInserted) {
                Toast.makeText(this, "Product added to cart", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, AddCartActivity::class.java)
                startActivity(intent)
            }

            else {
                Toast.makeText(this, "Failed to add product to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
