package com.vp.launcherapp.data.repository

import android.content.Context
import android.util.Log
import com.vp.launchersdk.MyPackageManager
import com.vp.launchersdk.model.AppInfo
import com.vp.launchersdk.receiver.notifyForAddition
import com.vp.launchersdk.receiver.notifyForDeletion
import com.vp.launchersdk.receiver.registerBroadCast
import com.vp.launchersdk.receiver.unregisterBroadCast
import com.vp.launcherapp.data.PackageUtils
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vishwanath Patil on 02/10/20.
 */
@Singleton
class LauncherRepository @Inject constructor(val packageUtils: PackageUtils)   {



    fun getData() : Observable<MutableList<AppInfo>>? {
        return packageUtils.getInstalledApps()
    }

    fun registerBroadCastLibrary(context: Context) {
        registerBroadCast(context)
    }

    fun unRegisterBroadCastLibrary(context: Context) {
        unregisterBroadCast(context)
    }

    fun checkForDeletion()  : Observable<Boolean>{
        Log.v("LauncherRepository","checkForDeletion()")
        return notifyForDeletion("repository")
    }

    fun checkForAddition()  : Observable<Boolean>{
        return notifyForAddition()
    }



}