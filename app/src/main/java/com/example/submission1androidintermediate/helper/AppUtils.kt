package com.example.submission1androidintermediate.helper

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.request.RequestOptions
import com.example.submission1androidintermediate.R
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter

object AppUtils {
    fun Activity.showToast(message: String) {
        showCustomToast(message, this)
    }

    fun Fragment.showToast(message: String) {
        showCustomToast(message, this.requireContext())
    }

    fun showCustomToast(message: String, context: Context) {
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

    fun Fragment.navigateToDestination(
        @IdRes dest: Int? = null,
        navOptions: NavOptions? = null,
        navDirections: NavDirections? = null
    ) {
        if (dest != null && navOptions != null) {
            this.findNavController().navigate(dest, null, navOptions)
        } else if (navDirections != null) {
            this.findNavController().navigate(navDirections)
        } else if (dest != null) this.findNavController().navigate(dest)
    }

    private fun getCircularProgressBar(context: Context): CircularProgressDrawable {
        val progressBar = CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }
        return progressBar
    }

    fun getGlideRequestOption(context: Context): RequestOptions {
        return RequestOptions.placeholderOf(getCircularProgressBar(context))
    }

    fun getDate(input: String?): String {
        return try {
            val date = ZonedDateTime.parse(input)
            val formatter = DateTimeFormatter.ofPattern("MMM dd yyyy HH:mm:ss")
            val formattedDate = date.format(formatter)
            formattedDate
        } catch (e: Exception) {
            e.printStackTrace()
            "N/A"
        }
    }


    fun Float.marginInDp(context: Context): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, this, context.resources
                .displayMetrics
        ).toInt()
    }
}
