package com.example.crud

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AgregarProductoActivity : AppCompatActivity() {

    private lateinit var dbHelper: MyDatabaseHelper
    private var idProducto: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_producto)


        findViewById<Button>(R.id.btnDevolverse).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        dbHelper = MyDatabaseHelper(this)

        // Referenciar elementos de la interfaz
        val nombreEditText = findViewById<EditText>(R.id.nombre)
        val precioEditText = findViewById<EditText>(R.id.precio)
        val cantidadEditText = findViewById<EditText>(R.id.cantidad)
        val proveedorEditText = findViewById<EditText>(R.id.idProveedor)
        val guardarButton = findViewById<Button>(R.id.btnSave)
        val eliminarButton = findViewById<Button>(R.id.btnDelete)

        // Obtener el ID del producto (si es un producto existente)
        idProducto = intent.getIntExtra("PRODUCTO_ID", -1).takeIf { it != -1 }

        // Si se edita un producto, cargar los datos
        idProducto?.let { id ->
            val producto = dbHelper.obtenerProductos().firstOrNull { it.id == id }
            producto?.let {
                nombreEditText.setText(it.nombre)
                precioEditText.setText(it.precio.toString())
                cantidadEditText.setText(it.cantidad.toString())
                proveedorEditText.setText(it.idProveedor.toString())
            }
        }

        // Configurar el evento del botón "Guardar"
        guardarButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val precio = precioEditText.text.toString().toDoubleOrNull() ?: 0.0
            val cantidad = cantidadEditText.text.toString().toIntOrNull() ?: 0
            val proveedor = proveedorEditText.text.toString().toIntOrNull() ?: 0

            if (nombre.isBlank()) {
                Toast.makeText(this, "El nombre del producto no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (idProducto != null) {
                dbHelper.actualizarProducto(
                    id = idProducto!!,
                    nombre = nombre,
                    precio = precio,
                    cantidad = cantidad,
                    idProveedor = proveedor
                )
                Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.insertarProducto(
                    nombre = nombre,
                    precio = precio,
                    cantidad = cantidad,
                    idProveedor = proveedor
                )
                Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

        // Configurar el evento del botón "Eliminar"
        eliminarButton.setOnClickListener {
            idProducto?.let { id ->
                dbHelper.eliminarProducto(id)
                Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
                finish()
            } ?: Toast.makeText(this, "No se puede eliminar un producto nuevo", Toast.LENGTH_SHORT).show()
        }
    }
}
