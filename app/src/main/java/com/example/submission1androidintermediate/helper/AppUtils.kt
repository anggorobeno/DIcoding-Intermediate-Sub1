package com.example.submission1androidintermediate.helper

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.submission1androidintermediate.R

object AppUtils {
    fun Activity.showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.showToast(message: String) {
        val view: View = LayoutInflater.from(context).inflate(R.layout.custom_toast, null)
        val text = view.findViewById<TextView>(R.id.toast_message)
        text.text = message
        text.background =
            AppCompatResources.getDrawable(context!!, R.drawable.bg_toast)

        val marginBottom = context!!.resources.getDimension(R.dimen.margin_48).toInt()

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = view
        toast.setGravity(Gravity.BOTTOM, 0, marginBottom)
        toast.show()
    }

    fun Fragment.navigateToDestination(@IdRes dest: Int, navOptions: NavOptions? = null) {
        if (navOptions != null) {
            this.findNavController().navigate(dest, null, navOptions)
        }
        else this.findNavController().navigate(dest)
    }

    fun Fragment.getNavGraph(): NavGraph {
        return this.findNavController().graph
    }
}