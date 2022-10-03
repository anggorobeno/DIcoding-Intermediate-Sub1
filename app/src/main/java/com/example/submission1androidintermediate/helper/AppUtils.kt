package com.example.submission1androidintermediate.helper

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

object AppUtils {
    fun Activity.showToast(message: String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }

    fun Fragment.showToast(message: String){
        Toast.makeText(this.requireActivity(),message,Toast.LENGTH_SHORT).show()
    }
}