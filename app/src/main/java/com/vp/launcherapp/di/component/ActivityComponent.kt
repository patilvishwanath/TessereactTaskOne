package com.vp.launcherapp.di.component

import com.vp.launcherapp.di.ActivityScope
import com.vp.launcherapp.di.module.ActivityModule
import com.vp.launcherapp.ui.LauncherActivity
import dagger.Component

/**
 * Created by Vishwanath Patil on 02/10/20.
 */
@ActivityScope
@Component(dependencies = [ApplciationComponent::class],modules = [ActivityModule::class])
interface ActivityComponent  {

    fun inject(launcherActivity: LauncherActivity)
}