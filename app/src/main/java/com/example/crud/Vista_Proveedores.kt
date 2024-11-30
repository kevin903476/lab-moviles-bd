package com.example.crud

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Vista_Proveedores : AppCompatActivity() {

    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var adapter: ProveedorAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vista_proveedores)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = MyDatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configura el botón flotante para agregar un nuevo producto
        findViewById<FloatingActionButton>(R.id.agregarProveedor).setOnClickListener {
            startActivity(Intent(this, AgregarProveedorActivity::class.java))
        }
        findViewById<FloatingActionButton>(R.id.btnProducto).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        // Cargar los productos en el RecyclerView
        loadProveedores()
    }

    private fun loadProveedores() {
        // Obtener productos de la base de datos y configurar el adaptador
        val proveedor = dbHelper.obtenerProveedores()
        adapter = ProveedorAdapter(
            proveedor,
            onProveedorClick = { proveedor -> editProveedor(proveedor.id) },
            onProveedorLongClick = { proveedor -> deleteProveedor(proveedor) }
        )
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadProveedores() // Refrescar la lista de productos cuando la actividad se reanuda
    }

    private fun editProveedor(proveedorId: Int) {
        // Iniciar actividad para editar un proveedor existente
        val intent = Intent(this, AgregarProveedorActivity::class.java).apply {
            putExtra("PROVEEDOR_ID", proveedorId) // Pasar el ID del proveedor
        }
        startActivity(intent)
    }

    private fun deleteProveedor(proveedor: Proveedor) {
        // Eliminar producto de la base de datos
        dbHelper.eliminarProveedor(proveedor.id)
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
        loadProveedores() // Refrescar lista después de eliminar
    }
}