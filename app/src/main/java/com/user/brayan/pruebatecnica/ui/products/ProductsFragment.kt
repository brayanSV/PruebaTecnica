package com.user.brayan.pruebatecnica.ui.products

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.user.brayan.pruebatecnica.Adapters.Adapter_Productos
import com.user.brayan.pruebatecnica.Model.Products
import com.user.brayan.pruebatecnica.R
import com.user.brayan.pruebatecnica.Remote.ApiUtils
import com.user.brayan.pruebatecnica.Remote.UserService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsFragment : Fragment() {
    private var service : UserService? = null
    private var rvProductos : RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        val root = inflater.inflate(R.layout.fragment_productos, container, false)

        this.service = ApiUtils.getService()
        this.rvProductos = root.findViewById(R.id.rvProductos)
        doProducts();
        return root
    }

    private fun doProducts() {
        val call = this.service?.productsData()
        call?.enqueue(object : Callback<ArrayList<Products>> {
            override fun onResponse(call: Call<ArrayList<Products>>, response: Response<ArrayList<Products>>) {
                if (response.isSuccessful) {
                    var listProducts : ArrayList<Products>? = response.body()

                    if (listProducts != null) {
                        loadProducts(listProducts)
                    } else {
                        //sin datos
                    }
                }
            }

            override fun onFailure(call: Call<ArrayList<Products>>, t: Throwable) {

            }
        })
    }

    private fun loadProducts(listProducts : ArrayList<Products>) {
        this.rvProductos?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val myAdapter = Adapter_Productos(listProducts)
        this.rvProductos?.adapter = myAdapter
    }
}