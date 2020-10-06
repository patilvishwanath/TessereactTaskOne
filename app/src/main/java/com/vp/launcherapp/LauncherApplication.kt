package com.vp.launcherapp

import android.app.Application
import com.vp.launcherapp.di.component.ApplciationComponent
import com.vp.launcherapp.di.component.DaggerApplciationComponent
import com.vp.launcherapp.di.module.ApplicationModule

import com.vp.launcherapp.ui.LauncherViewModel

import javax.inject.Inject

/**
 * Created by Vishwanath Patil on 02/10/20.
 */
class LauncherApplication : Application() {

    lateinit var appComponent: ApplciationComponent

    @Inject
    lateinit var viewModel: LauncherViewModel

    override fun onCreate() {
        super.onCreate()
        setUpDependencies()
        registerAppBroadCast()
      //  getDataFromManager()

    }



    private fun setUpDependencies() {
        appComponent =  DaggerApplciationComponent
            .builder()
            .applicationModule(
                ApplicationModule(
                    this
                )
            )
            .build()

        appComponent.inject(this)

    }

    private fun registerAppBroadCast() {
       appComponent.getLauncherRepo().registerBroadCastLibrary(applicationContext)
    }

     fun unregisterAppBroadCast(){
        appComponent.getLauncherRepo().unRegisterBroadCastLibrary(applicationContext)
    }


    



}