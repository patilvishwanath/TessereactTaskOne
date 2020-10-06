package com.vp.launchersdk

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import com.vp.launchersdk.model.AppInfo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers

/**
 * Created by Vishwanath Patil on 02/10/20.
 */
class MyPackageManager(val context: Context) {


    companion object {
        const val TAG = "MyPackageManager"
    }


    fun getListOfApps(): Observable<MutableList<AppInfo>>? {

        val packageManager = context.packageManager

        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)

        return Observable
            .fromArray(packageManager.queryIntentActivities(intent, 0))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(Schedulers.computation())
            ?.map(object : Function<MutableList<ResolveInfo>, MutableList<AppInfo>> {
                override fun apply(t: MutableList<ResolveInfo>): MutableList<AppInfo> {
                    Log.v(TAG, "Schedulers.computation() ${Thread.currentThread().name}")
                    return convertData(t, packageManager)
                }

            })
            ?.observeOn(AndroidSchedulers.mainThread())


    }

    private fun convertData(
        allApps: MutableList<ResolveInfo>,
        packageManager: PackageManager
    ): MutableList<AppInfo> {

        Log.v(TAG, "${allApps.size}")

        val appInfoList = mutableListOf<AppInfo>()

        for (i in allApps) {
            val app = AppInfo(
                i.loadLabel(packageManager).toString(),
                i.activityInfo.packageName,
                i.loadIcon(packageManager),
                i.activityInfo.name,
                packageManager.getPackageInfo(
                    i.activityInfo.packageName,
                    0
                ).versionCode.toString(),
                packageManager.getPackageInfo(
                    i.activityInfo.packageName,
                    0
                ).versionName
            )


            appInfoList.add(app)
            Log.v(TAG, app.toString())
        }




        return appInfoList.sortedBy { it.appName }.toMutableList()


    }
}