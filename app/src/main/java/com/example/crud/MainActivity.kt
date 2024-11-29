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

private infix fun Unit.loadProducts(unit: Unit) {


}

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var adapter: ProductAdapter
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
        findViewById<FloatingActionButton>(R.id.fabAddProduct).setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
        loadProducts()

    }
    private fun loadProducts() {// Obtener productos de la base de datos y configurar el adaptador
        val products = dbHelper.getAllProducts()
        adapter = ProductAdapter(products,
            onProductClick = { product -> editProduct(product.id) },
            onProductLongClick = { product -> deleteProduct(product) }
        )
        recyclerView.adapter = adapter}
    override fun onResume() {
        super.onResume()
        loadProducts()    }
    private fun editProduct(productId: Int) {
        // Iniciar actividad para editar un producto existente
        val intent = Intent(this, AddProductActivity::class.java).apply {
            putExtra("PRODUCT_ID", productId)
        }
        startActivity(intent)
    }

    private fun deleteProduct(product: Product) {
        // Eliminar producto de la base de datos
        dbHelper.deleteProduct(product.id)
        Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show()
        loadProducts() // Refrescar lista después de eliminar
    }

}