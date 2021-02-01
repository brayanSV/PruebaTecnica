package com.user.brayan.pruebatecnica.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.user.brayan.pruebatecnica.Model.Products
import com.user.brayan.pruebatecnica.R

class Adapter_Productos(val listProducts : ArrayList<Products>) : RecyclerView.Adapter<Adapter_Productos.ProductsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        return ProductsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardview_productos, parent, false))
    }

    override fun getItemCount(): Int {
        return listProducts.size
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        holder.binItems(listProducts[position])
    }

    class ProductsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun binItems(product: Products) {
            val tvTitulo : TextView = itemView.findViewById(R.id.tvTitulo)
            val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
            val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)

            tvTitulo.text = product.Name
            tvDescripcion.text = product.Description
            Picasso.with(itemView.context).load(product.ImageUrl).into(imgProducto)
        }
    }
}