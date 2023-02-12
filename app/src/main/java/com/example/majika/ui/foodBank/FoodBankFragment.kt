package com.example.majika.ui.foodBank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.majika.databinding.FragmentFoodBankBinding
import com.example.majika.repository.Repository

class FoodBankFragment : Fragment() {

    private var _binding: FragmentFoodBankBinding? = null

    private val menuAdapter by lazy { MenuAdapter() }
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.menuList.layoutManager = LinearLayoutManager(context)
        binding.menuList.adapter = menuAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val repository = Repository()
        val viewModelFactory = FoodBankViewModelFactory(repository)
        val foodBankViewModel =
            ViewModelProvider(this, viewModelFactory)[FoodBankViewModel::class.java]
        foodBankViewModel.getMenus()
        foodBankViewModel.menuRes.observe(viewLifecycleOwner, Observer { response ->
            if (response.isSuccessful) {
                response.body()?.data.let {
                    if (it != null) {
                        menuAdapter.showData(it)
                    }
                }
            }
        })

        _binding = FragmentFoodBankBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}