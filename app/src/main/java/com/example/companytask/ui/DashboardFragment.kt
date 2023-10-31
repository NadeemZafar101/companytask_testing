package com.example.companytask.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.companytask.R
import com.example.companytask.databinding.FragmentDashboardBinding


class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private val galleryPermissionCode = 101
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentDashboardBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.roomDbButton.setOnClickListener {
            permission(R.id.action_dashboardFragment_to_crudOperationFragment)
        }
        binding.canvasDrawingButton.setOnClickListener {
            permission(R.id.action_dashboardFragment_to_canvasDrawingFragment)
        }
    }
    private fun navigate(id:Int){
        findNavController().navigate(id)
    }
    private fun permission(id:Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                navigate(id)
            } else { ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, galleryPermissionCode) }
        }else{
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                navigate(id)
            } else { ActivityCompat.requestPermissions(requireActivity(), PERMISSIONS, galleryPermissionCode) }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == galleryPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    requireContext(),
                    "Permission Granted Click again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    private val PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            android.Manifest.permission.READ_MEDIA_IMAGES,
        )
    } else {
        arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }
}