package com.example.store.ui.checkout

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
import com.example.store.databinding.FragmentCheckoutBinding

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private lateinit var callingActivity: MainActivity
    private val storeViewModel: StoreViewModel by activityViewModels()

    private lateinit var checkoutAdapter: CheckoutAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        callingActivity = activity as MainActivity



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkoutAdapter = CheckoutAdapter(callingActivity, this)
        binding.cartRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.cartRecyclerView.itemAnimator = DefaultItemAnimator()
        binding.cartRecyclerView.adapter = checkoutAdapter
        storeViewModel.products.observe(viewLifecycleOwner) { products ->
            products?.let {
                val basket = it.filter { p ->
                    p.inCart
                }
                if (basket.isEmpty()) binding.emptyCart.visibility = View.VISIBLE
                else binding.emptyCart.visibility = View.GONE
                val adapterBasket = checkoutAdapter.products
                when {
                    basket.size > adapterBasket.size -> {
                        val newProducts = basket - adapterBasket
                        for (p in newProducts) {
                            checkoutAdapter.products.add(p)
                            checkoutAdapter.notifyItemInserted(checkoutAdapter.products.size - 1)
                        }
                    }
                    basket.size < adapterBasket.size -> {
                        val removedProducts = adapterBasket - basket
                        for (p in removedProducts) {
                            val index = checkoutAdapter.products.indexOf(p)
                            checkoutAdapter.products.removeAt(index)
                            checkoutAdapter.notifyItemRemoved(index)
                        }
                    }
                    adapterBasket.isEmpty() && basket.isNotEmpty() -> {
                        checkoutAdapter.products = basket.toMutableList()
                        checkoutAdapter.notifyItemRangeInserted(0, basket.size)
                    }
                    basket.isEmpty() -> {
                        checkoutAdapter.notifyItemRangeRemoved(0, basket.size)
                        checkoutAdapter.products = mutableListOf()
                    }
                }
                updateOrderTotal()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}