package com.example.farmerapplication

import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileOutputStream

class DeliveryActivity : AppCompatActivity() {

    private lateinit var homeAddressEditText: EditText
    private lateinit var pincodeEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var stateRadioGroup: RadioGroup
    private lateinit var cityEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var paymentButton: Button
    private lateinit var userDatabaseHelper: UserDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery)


        homeAddressEditText = findViewById(R.id.homeAddressEditText)
        pincodeEditText = findViewById(R.id.pincodeEditText)
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText)
        stateRadioGroup = findViewById(R.id.stateRadioGroup)
        cityEditText = findViewById(R.id.cityEditText)
        submitButton = findViewById(R.id.submitButton)
        paymentButton = findViewById(R.id.paymentButton)

        userDatabaseHelper = UserDatabaseHelper(this)


        submitButton.setOnClickListener {
            try {
                val homeAddress = homeAddressEditText.text.toString()
                val pincode = pincodeEditText.text.toString()
                val phoneNumber = phoneNumberEditText.text.toString()
                val selectedState = getSelectedState()
                val selectedCity = cityEditText.text.toString()


                if (homeAddress.isEmpty() || pincode.isEmpty() || phoneNumber.isEmpty() ||
                    selectedState.isEmpty() || selectedCity.isEmpty()
                ) {
                    Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show()
                } else if (!isValidPincode(pincode)) {
                    Toast.makeText(
                        this,
                        "Please enter a valid 6-digit pincode.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (!isValidPhoneNumber(phoneNumber)) {
                    Toast.makeText(
                        this,
                        "Please enter a valid 10-digit phone number.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {

                    val username = "user_${System.currentTimeMillis()}"
                    val password = "password"
                    userDatabaseHelper.addUser(username, password, phoneNumber, homeAddress)

                    Toast.makeText(this, "Order Submitted Successfully!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: SQLiteConstraintException) {
                Log.e("DeliveryActivity", " Database Error : ${e.message}", e)
                Toast.makeText(
                    this,
                    "This username is already exists. Please try again.",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Log.e("DeliveryActivity", "Error: ${e.message}", e)
                Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        paymentButton.setOnClickListener {
            val url = "https://paytm.com/online-payments"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }


    private fun generateItemSlip(
        productName: String,
        productPrice: Int,
        productQuantity: Int,
        totalPrice: Int,
        deliveryAddress: String
    ) {
        try {
            val slipContent = """
                ***** Order Slip *****
                Product Name: $productName
                Product Price: ₹$productPrice /kg
                Quantity: $productQuantity kg
                Total Price: ₹$totalPrice
                
                Delivery Address:
                $deliveryAddress

                Thank you for your order!
            """.trimIndent()

            val fileName = "order_slip.txt"
            val file = File(getExternalFilesDir(null), fileName)
            val fileOutputStream = FileOutputStream(file)
            fileOutputStream.write(slipContent.toByteArray())
            fileOutputStream.close()

            Toast.makeText(this, "Slip saved at ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("DeliveryActivity", "Error generating slip: ${e.message}", e)
            Toast.makeText(this, "Error generating slip", Toast.LENGTH_SHORT).show()
        }
    }


    private fun getSelectedState(): String {
        val selectedId = stateRadioGroup.checkedRadioButtonId
        return if (selectedId != -1) {
            val radioButton: RadioButton = findViewById(selectedId)
            radioButton.text.toString()
        } else {
            ""
        }
    }


    private fun isValidPincode(pincode: String): Boolean {
        return pincode.length == 6 && pincode.all { it.isDigit() }
    }


    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }
    }
}
