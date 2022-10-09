package com.example.submission1androidintermediate.helper

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Environment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.request.RequestOptions
import com.example.submission1androidintermediate.R
import com.example.submission1androidintermediate.ui.home.stories.camera.CameraFragment
import com.google.android.material.progressindicator.CircularProgressIndicator
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

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

    fun Fragment.getNavGraph(): NavGraph {
        return this.findNavController().graph
    }

    fun RecyclerView.smoothSnapToPosition(
        position: Int,
        snapMode: Int = LinearSmoothScroller.SNAP_TO_START
    ) {
        val smoothScroller = object : LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int = snapMode
            override fun getHorizontalSnapPreference(): Int = snapMode
        }
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
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
}
