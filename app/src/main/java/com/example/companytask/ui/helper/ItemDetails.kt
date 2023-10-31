package com.example.companytask.ui.helper

import android.widget.ImageView
import android.widget.TextView

interface ItemDetails {
    fun getItemDetails(position:Int,editButton:ImageView,deleteButton:ImageView)
}