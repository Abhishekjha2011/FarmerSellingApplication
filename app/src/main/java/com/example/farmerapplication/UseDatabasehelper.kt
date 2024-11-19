package com.example.farmerapplication

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class UserDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "user_data.db"
        private const val DATABASE_VERSION = 1

        const val TABLE_NAME = "users"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_PHONE_NUMBER = "phone_number"
        const val COLUMN_ADDRESS = "address"

        // Cart table columns
        const val CART_TABLE_NAME = "cart"
        const val CART_COLUMN_ID = "id"
        const val CART_COLUMN_PRODUCT_NAME = "product_name"
        const val CART_COLUMN_PRODUCT_PRICE = "product_price"
        const val CART_COLUMN_QUANTITY = "quantity"
    }

    override fun onCreate(db: SQLiteDatabase?) {

        val CREATE_USERS_TABLE = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_USERNAME TEXT PRIMARY KEY, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_PHONE_NUMBER TEXT, " +
                "$COLUMN_ADDRESS TEXT)"

        val CREATE_CART_TABLE = "CREATE TABLE $CART_TABLE_NAME (" +
                "$CART_COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$CART_COLUMN_PRODUCT_NAME TEXT, " +
                "$CART_COLUMN_PRODUCT_PRICE TEXT, " +
                "$CART_COLUMN_QUANTITY INTEGER)"

        db?.execSQL(CREATE_USERS_TABLE)
        db?.execSQL(CREATE_CART_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $CART_TABLE_NAME")
        onCreate(db)
    }

    fun addUser(username: String, password: String, phoneNumber: String, address: String) {
        val db = this.writableDatabase
        val query = "INSERT INTO $TABLE_NAME ($COLUMN_USERNAME, $COLUMN_PASSWORD, $COLUMN_PHONE_NUMBER, $COLUMN_ADDRESS) " +
                "VALUES ('$username', '$password', '$phoneNumber', '$address')"
        db.execSQL(query)
        db.close()
    }

    fun checkUser(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val userExists = cursor.count > 0
        cursor.close()
        db.close()
        return userExists
    }


    fun validateCredentials(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val isValid = cursor.count > 0
        cursor.close()
        db.close()
        return isValid
    }


    fun addProductToCart(productName: String, productPrice: String, quantity: Int): Boolean {
        val db = this.writableDatabase
        val query = "INSERT INTO $CART_TABLE_NAME ($CART_COLUMN_PRODUCT_NAME, $CART_COLUMN_PRODUCT_PRICE, $CART_COLUMN_QUANTITY) " +
                "VALUES ('$productName', '$productPrice', $quantity)"
        db.execSQL(query)
        db.close()
        return true
    }

    fun getCartItems(): List<CartItem> {
        val cartItems = mutableListOf<CartItem>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $CART_TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            val idColumnIndex = cursor.getColumnIndex(CART_COLUMN_ID)
            val productNameColumnIndex = cursor.getColumnIndex(CART_COLUMN_PRODUCT_NAME)
            val productPriceColumnIndex = cursor.getColumnIndex(CART_COLUMN_PRODUCT_PRICE)
            val quantityColumnIndex = cursor.getColumnIndex(CART_COLUMN_QUANTITY)

            if (idColumnIndex >= 0 && productNameColumnIndex >= 0 &&
                productPriceColumnIndex >= 0 && quantityColumnIndex >= 0) {

                do {
                    val id = cursor.getInt(idColumnIndex)
                    val productName = cursor.getString(productNameColumnIndex)
                    val productPrice = cursor.getString(productPriceColumnIndex)
                    val quantity = cursor.getInt(quantityColumnIndex)
                    cartItems.add(CartItem(id, productName, productPrice, quantity))
                } while (cursor.moveToNext())
            } else {

                Log.e("DatabaseError", "Invalid column index in cart table")
            }
        }
        cursor.close()
        db.close()

        return cartItems
    }


    fun removeProductFromCart(cartItemId: Int): Boolean {
        val db = this.writableDatabase
        val query = "DELETE FROM $CART_TABLE_NAME WHERE $CART_COLUMN_ID = $cartItemId"
        db.execSQL(query)
        db.close()
        return true
    }
}

data class CartItem(
    val id: Int,
    val productName: String,
    val productPrice: String,
    val quantity: Int
)
