package com.example.store.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.store.MainActivity
import com.example.store.StoreViewModel
import com.example.store.databinding.FragmentProductsBinding

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private lateinit var callingActivity: MainActivity
    private val storeViewModel: StoreViewModel by activityViewModels()
    private lateinit var productsAdapter: ProductsAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        callingActivity = activity as MainActivity

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storeViewModel.currency.observe(viewLifecycleOwner) { currency ->
            currency?.let {
                binding.productsRecyclerView.visibility = View.VISIBLE
                binding.loadingProgress.visibility = View.GONE
                // Detect whether the selected currency different than the currency currently being used
                if (productsAdapter.currency == null || it.symbol != productsAdapter.currency?.symbol){
                    productsAdapter.currency = it
                    productsAdapter.notifyItemRangeChanged(0, productsAdapter.itemCount)
                }
            }
        }
        productsAdapter = ProductsAdapter(callingActivity, this)
        binding.productsRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.productsRecyclerView.itemAnimator = DefaultItemAnimator()
        binding.productsRecyclerView.adapter = productsAdapter
        productsAdapter.products = storeViewModel.products.value?.toMutableList() ?: mutableListOf()
        productsAdapter.notifyItemRangeInserted(0, productsAdapter.products.size)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun updateCart(index: Int) {
        val products = productsAdapter.products.toMutableList()
        products[index].inCart = !products[index].inCart
        productsAdapter.products = products
// Call notifyItemChanged to update the add to basket button for that product
        productsAdapter.notifyItemChanged(index)
        storeViewModel.products.value = products
        //storeViewModel.calculateOrderTotal()
    }

}