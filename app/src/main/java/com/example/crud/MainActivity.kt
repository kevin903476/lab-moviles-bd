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

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var adapter: ProductoAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = MyDatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configura el botón flotante para agregar un nuevo producto
        findViewById<FloatingActionButton>(R.id.agregarProducto).setOnClickListener {
            startActivity(Intent(this, AgregarProductoActivity::class.java))
        }
        findViewById<FloatingActionButton>(R.id.btnProveedor).setOnClickListener {
            startActivity(Intent(this, Vista_Proveedores::class.java))
        }

        // Cargar los productos en el RecyclerView
        loadProducts()
    }

    private fun loadProducts() {
        // Obtener productos de la base de datos y configurar el adaptador
        val products = dbHelper.obtenerProductos()
        adapter = ProductoAdapter(
            products,
            onProductoClick = { producto -> editProduct(producto.id) },
            onProductoLongClick = { producto -> deleteProduct(producto) }
        )
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadProducts() // Refrescar la lista de productos cuando la actividad se reanuda
    }

    private fun editProduct(productId: Int) {
        // Iniciar actividad para editar un producto existente
        val intent = Intent(this, AgregarProductoActivity::class.java).apply {
            putExtra("PRODUCTO_ID", productId) // Pasar el ID del producto
        }
        startActivity(intent)
    }

    private fun deleteProduct(product: Producto) {
        // Eliminar producto de la base de datos
        dbHelper.eliminarProducto(product.id)
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
        loadProducts() // Refrescar lista después de eliminar
    }
}
