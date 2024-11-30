package com.example.crud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapter(
    private val productos: List<Producto>,
    private val onProductoClick: ((Producto) -> Unit)? = null, // Evento opcional al hacer clic
    private val onProductoLongClick: ((Producto) -> Unit)? = null // Evento opcional al hacer clic prolongado
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    inner class ProductoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombre)
        val precioTextView: TextView = view.findViewById(R.id.precio)
        val cantidadTextView: TextView = view.findViewById(R.id.cantidad)
        val proveedorTextView: TextView = view.findViewById(R.id.idProveedor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productos[position]
        holder.nombreTextView.text = producto.nombre
        holder.precioTextView.text = "Precio: ${producto.precio}"
        holder.cantidadTextView.text = "Cantidad: ${producto.cantidad}"
        holder.proveedorTextView.text = "Proveedor ID: ${producto.idProveedor}"

        // Configurar eventos de clic y clic prolongado
        holder.itemView.setOnClickListener {
            onProductoClick?.invoke(producto) // Llamar al callback de clic si está definido
        }
        holder.itemView.setOnLongClickListener {
            onProductoLongClick?.invoke(producto) // Llamar al callback de clic prolongado si está definido
            true // Indicar que el evento ha sido manejado
        }
    }

    override fun getItemCount() = productos.size
}
