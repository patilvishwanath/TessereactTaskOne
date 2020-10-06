package com.vp.launcherapp.di.component

import com.vp.launcherapp.LauncherApplication
import com.vp.launcherapp.data.PackageUtils
import com.vp.launcherapp.data.repository.LauncherRepository
import com.vp.launcherapp.di.module.ApplicationModule
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Vishwanath Patil on 02/10/20.
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplciationComponent {

    fun inject(app: LauncherApplication)

    fun getLauncherRepo() : LauncherRepository

    fun getPackageUtils() : PackageUtils

}