package com.example.crud

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AgregarProveedorActivity : AppCompatActivity() {

    private lateinit var dbHelper: MyDatabaseHelper
    private var idProveedor: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_proveedor)


        findViewById<Button>(R.id.btnDevolverse).setOnClickListener {
            startActivity(Intent(this, Vista_Proveedores::class.java))
        }
        dbHelper = MyDatabaseHelper(this)

        // Referenciar elementos de la interfaz
        val nombreEditText = findViewById<EditText>(R.id.nombre)
        val empresaEditText = findViewById<EditText>(R.id.empresa)
        val guardarButton = findViewById<Button>(R.id.btnSave)
        val eliminarButton = findViewById<Button>(R.id.btnDelete)

        // Obtener el ID del proveedor (si es un proveedor existente)
        idProveedor = intent.getIntExtra("PROVEEDOR_ID", -1).takeIf { it != -1 }

        // Si se edita un proveedor, cargar los datos
        idProveedor?.let { id ->
            val proveedor = dbHelper.obtenerProveedores().firstOrNull { it.id == id }
            proveedor?.let {
                nombreEditText.setText(it.nombre)
                empresaEditText.setText(it.empresa)
            }
        }

        // Configurar el evento del botón "Guardar"
        guardarButton.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val empresa = empresaEditText.text.toString()

            if (nombre.isBlank()) {
                Toast.makeText(this, "El nombre del proveedor no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (idProveedor != null) {
                dbHelper.actualizarProveedor(
                    id = idProveedor!!,
                    nombre = nombre,
                    empresa = empresa
                )
                Toast.makeText(this, "Proveedor actualizado", Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.insertarProveedor(
                    nombre = nombre,
                    empresa = empresa,
                )
                Toast.makeText(this, "Proveedor agregado", Toast.LENGTH_SHORT).show()
            }
            finish()
        }

        // Configurar el evento del botón "Eliminar"
        eliminarButton.setOnClickListener {
            idProveedor?.let { id ->
                dbHelper.eliminarProveedor(id)
                Toast.makeText(this, "Proveedor eliminado", Toast.LENGTH_SHORT).show()
                finish()
            } ?: Toast.makeText(this, "No se puede eliminar un proveedor nuevo", Toast.LENGTH_SHORT).show()
        }
    }
}
