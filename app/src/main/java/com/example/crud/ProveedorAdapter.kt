package com.example.crud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProveedorAdapter(
    private val proveedores: List<Proveedor>,
    private val onProveedorClick: ((Proveedor) -> Unit)? = null, // Evento opcional al hacer clic
    private val onProveedorLongClick: ((Proveedor) -> Unit)? = null // Evento opcional al hacer clic prolongado
) : RecyclerView.Adapter<ProveedorAdapter.ProveedorViewHolder>() {

    inner class ProveedorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombre)
        val empresaTextView: TextView = view.findViewById(R.id.empresa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProveedorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_proveedor, parent, false)
        return ProveedorViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProveedorViewHolder, position: Int) {
        val proveedor = proveedores[position]
        holder.nombreTextView.text = proveedor.nombre
        holder.empresaTextView.text = "Empresa: ${proveedor.empresa}"

        // Configurar eventos de clic y clic prolongado
        holder.itemView.setOnClickListener {
            onProveedorClick?.invoke(proveedor) // Llamar al callback de clic si está definido
        }
        holder.itemView.setOnLongClickListener {
            onProveedorLongClick?.invoke(proveedor) // Llamar al callback de clic prolongado si está definido
            true // Indicar que el evento ha sido manejado
        }
    }

    override fun getItemCount() = proveedores.size
}
