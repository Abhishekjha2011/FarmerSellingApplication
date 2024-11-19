package com.example.farmerapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class DashboardActivity : AppCompatActivity() {

    private val products = mutableListOf<Product>(
        Product("Apple", R.drawable.apple, 120.0, 0),
        Product("Banana", R.drawable.banana, 60.0, 0),
        Product("Chilli", R.drawable.chilli, 50.0, 0),
        Product("Potato", R.drawable.potato, 80.0, 0),
        Product("Onion", R.drawable.onion, 100.0, 0),
        Product("Carrot", R.drawable.carrot, 70.0, 0),
        Product("Grapes", R.drawable.grapes, 150.0, 0),
        Product("Tomato", R.drawable.tomato, 120.0, 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        setupProductUI(products[0], findViewById(R.id.productImage1), findViewById(R.id.addButton1), findViewById(R.id.minusButton1), findViewById(R.id.quantityText1))
        setupProductUI(products[1], findViewById(R.id.productImage2), findViewById(R.id.addButton2), findViewById(R.id.minusButton2), findViewById(R.id.quantityText2))
        setupProductUI(products[2], findViewById(R.id.productImage3), findViewById(R.id.addButton3), findViewById(R.id.minusButton3), findViewById(R.id.quantityText3))
        setupProductUI(products[3], findViewById(R.id.productImage4), findViewById(R.id.addButton4), findViewById(R.id.minusButton4), findViewById(R.id.quantityText4))
        setupProductUI(products[4], findViewById(R.id.productImage5), findViewById(R.id.addButton5), findViewById(R.id.minusButton5), findViewById(R.id.quantityText5))
        setupProductUI(products[5], findViewById(R.id.productImage6), findViewById(R.id.addButton6), findViewById(R.id.minusButton6), findViewById(R.id.quantityText6))


        val viewCartButton: Button = findViewById(R.id.viewCartButton)
        viewCartButton.setOnClickListener {
            val intent = Intent(this, CartListActivity::class.java)

            val totalPrice = calculateTotalPrice()
            val selectedProductDetails = products.filter { it.quantity > 0 }


            val selectedProductImages = selectedProductDetails.map { it.imageResId }
            val selectedProductNames = selectedProductDetails.map { it.name }
            val selectedProductQuantities = selectedProductDetails.map { it.quantity }

            intent.putExtra("totalPrice", totalPrice)
            intent.putStringArrayListExtra("selectedProductNames", ArrayList(selectedProductNames))
            intent.putIntegerArrayListExtra("selectedProductImages", ArrayList(selectedProductImages))
            intent.putIntegerArrayListExtra("selectedProductQuantities", ArrayList(selectedProductQuantities))

            startActivity(intent)
        }
    }

    private fun setupProductUI(product: Product, productImage: ImageView, addButton: ImageView, minusButton: ImageView, quantityText: TextView) {
        productImage.setImageResource(product.imageResId)


        quantityText.text = product.quantity.toString()

        addButton.setOnClickListener {
            product.quantity++
            quantityText.text = product.quantity.toString()
            Toast.makeText(this, "${product.name} added! Quantity: ${product.quantity}", Toast.LENGTH_SHORT).show()
        }

        minusButton.setOnClickListener {
            if (product.quantity > 0) {
                product.quantity--
                quantityText.text = product.quantity.toString()
                Toast.makeText(this, "${product.name} removed! Quantity: ${product.quantity}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No ${product.name} to remove!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun calculateTotalPrice(): String {
        var total = 0.0
        for (product in products) {
            total += product.quantity * product.price
        }
        return "₹ ${total.toString()}"
    }
}
