package com.example.companytask.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.companytask.R
import com.example.companytask.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
    binding = FragmentSplashBinding.inflate(inflater,container,false)
    return binding.root
    }
    override fun onResume() {
        super.onResume()
        navigate()

    }
    private fun navigate(){
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_dashboardFragment)
        }, 2000)
    }

}