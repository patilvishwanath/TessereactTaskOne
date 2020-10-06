package com.vp.launcherapp.di.module

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.vp.launcherapp.data.repository.LauncherRepository
import com.vp.launcherapp.di.ActivityScope
import com.vp.launcherapp.ui.LauncherViewModel
import com.vp.launcherapp.utils.ViewModelProviderFactory
import dagger.Module
import dagger.Provides

/**
 * Created by Vishwanath Patil on 02/10/20.
 */
@Module
class ActivityModule(val activity: AppCompatActivity) {


    @Provides
    fun providesContext() = activity

    @Provides
    fun provideGridLayout() : LinearLayoutManager = LinearLayoutManager(activity)

//    @ActivityScope
//    @Provides
//    fun provideLauncherAdapter(activity: AppCompatActivity, list: PackageUtils) : LauncherAdapter {
//        return LauncherAdapter(activity,list)
//    }


    @ActivityScope
    @Provides
    fun provideLauncherViewModel(launcherRepository: LauncherRepository) : LauncherViewModel {
        return ViewModelProvider(
            activity,
            ViewModelProviderFactory(LauncherViewModel::class) {
                LauncherViewModel(launcherRepository)
            }).get(LauncherViewModel::class.java)
    }

}