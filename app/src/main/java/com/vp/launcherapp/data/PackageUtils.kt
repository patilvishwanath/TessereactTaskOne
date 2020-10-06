package com.vp.launcherapp.data

import android.content.Context
import com.vp.launchersdk.model.AppInfo
import com.vp.launchersdk.MyPackageManager
import com.vp.launcherapp.data.repository.LauncherRepository
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Vishwanath Patil on 02/10/20.
 */
@Singleton
class PackageUtils@Inject constructor(val context: Context) {

//    fun getInstalledApps() : Single<List<AppInfo>>? {
//        return MyPackageManager(context).getListOfApps()
//    }

    fun getInstalledApps() : Observable<MutableList<AppInfo>>? {
        return MyPackageManager(context).getListOfApps()
    }




}