package com.vp.launchersdk.model

import android.graphics.drawable.Drawable

/**
 * Created by Vishwanath Patil on 02/10/20.
 */
data class AppInfo(
    val appName: String,
    val packageName: String,
    val icon: Drawable,
    val mainActivity: String,
    val versionCode: String,
    val versionName: String
) {
}