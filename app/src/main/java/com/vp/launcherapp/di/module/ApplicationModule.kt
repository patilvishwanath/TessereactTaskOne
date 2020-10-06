package com.vp.launcherapp.di.module

import androidx.appcompat.app.AppCompatActivity
import com.vp.launcherapp.LauncherApplication
import com.vp.launcherapp.data.PackageUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by Vishwanath Patil on 02/10/20.
 */
@Module
class ApplicationModule(val application : LauncherApplication)  {


    @Provides
    fun providesContext() = application

    @Singleton
    @Provides
    fun getPackageUtils() : PackageUtils {
        return PackageUtils(application)
    }

}