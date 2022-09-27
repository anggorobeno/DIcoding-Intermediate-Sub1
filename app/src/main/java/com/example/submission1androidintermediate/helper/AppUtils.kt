package com.example.submission1androidintermediate.helper

import android.app.Activity
import android.widget.Toast

object AppUtils {
    fun Activity.showToast(message: String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}