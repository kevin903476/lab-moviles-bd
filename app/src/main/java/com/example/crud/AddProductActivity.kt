package com.example.crud

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddProductActivity : AppCompatActivity() {

    private lateinit var dbHelper: MyDatabaseHelper
    private var productId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = MyDatabaseHelper(this)
        val nameEditText = findViewById<EditText>(R.id.etName)
        val priceEditText = findViewById<EditText>(R.id.etPrice)
        val quantityEditText = findViewById<EditText>(R.id.etQuantity)
        val saveButton = findViewById<Button>(R.id.btnSave)
        val deleteButton = findViewById<Button>(R.id.btnDelete)

        productId = intent.getIntExtra("PRODUCT_ID", -1).takeIf { it != -1 }

        if (productId != null) {
            val product = dbHelper.getAllProducts().firstOrNull { it.id == productId }
            product?.let {
                nameEditText.setText(it.name)
                priceEditText.setText(it.price.toString())
                quantityEditText.setText(it.quantity.toString())
            }
        }

        saveButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val price = priceEditText.text.toString().toDoubleOrNull() ?: 0.0
            val quantity = quantityEditText.text.toString().toIntOrNull() ?: 0

            if (productId != null) {
                dbHelper.updateProduct(productId!!, name, price, quantity)
                Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.insertProduct(name = name, price = price, quantity = quantity)
                Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
            }

            finish()
        }

        deleteButton.setOnClickListener {
            if (productId != null) {
                dbHelper.deleteProduct(productId!!)
                Toast.makeText(this, "Producto eliminado" + "", Toast.LENGTH_SHORT).show()
            }

            finish()
        }
    }

}