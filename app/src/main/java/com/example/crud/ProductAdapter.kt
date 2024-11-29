package com.example.crud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter(
    private val products: List<Product>,
    private val onProductClick: ((Product) -> Unit)? = null, // Evento opcional al hacer clic
    private val onProductLongClick: ((Product) -> Unit)? = null // Evento opcional al hacer clic prolongado
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvName)
        val priceTextView: TextView = view.findViewById(R.id.tvPrice)
        val quantityTextView: TextView = view.findViewById(R.id.tvQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.nameTextView.text = product.name
        holder.priceTextView.text = "Price: ${product.price}"
        holder.quantityTextView.text = "Qty: ${product.quantity}"

        // Configurar eventos de clic y clic prolongado
        holder.itemView.setOnClickListener {
            onProductClick?.invoke(product) // Llamar al callback de clic si está definido
        }
        holder.itemView.setOnLongClickListener {
            onProductLongClick?.invoke(product) // Llamar al callback de clic prolongado si está definido
            true // Indicar que el evento ha sido manejado
        }
    }


    override fun getItemCount() = products.size
}
