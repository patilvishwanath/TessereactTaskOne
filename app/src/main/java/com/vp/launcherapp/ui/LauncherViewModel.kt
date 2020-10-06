package com.vp.launcherapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vp.launchersdk.model.AppInfo
import com.vp.launcherapp.data.repository.LauncherRepository
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import javax.inject.Inject

/**
 * Created by Vishwanath Patil on 02/10/20.
 */
class LauncherViewModel @Inject constructor(private val repository: LauncherRepository) :
    ViewModel() {

    private var _appData = MutableLiveData<List<AppInfo>>()
    val appData: LiveData<List<AppInfo>>
        get() = _appData

    private var _filterData = MutableLiveData<List<AppInfo>>()
    val filterData: LiveData<List<AppInfo>>
        get() = _filterData

    val appDeletedData = MutableLiveData<Boolean>()


    private lateinit var list: MutableList<AppInfo>

    companion object {
        const val TAG = "LauncherViewModel"
    }

    init {


        repository.getData()?.subscribe(object : Observer<MutableList<AppInfo>> {
            override fun onComplete() {
                Log.v(TAG, "onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Log.v(TAG, "onSubscribe")
            }

            override fun onNext(t: MutableList<AppInfo>) {
                list = t
                Log.v(TAG, "onNext ${t.size}")
                _appData.postValue(t)
            }

            override fun onError(e: Throwable) {
                Log.v(TAG, "onError ${e.message}")
            }


        })


    }

    fun filterApps(query: String) {

        if (query.isBlank()) {
            _filterData.postValue(list)
        } else {
            val filtered = ArrayList<AppInfo>()
            for (i in list) {
                if (i.appName.toLowerCase().contains(query.toLowerCase())) {
                    filtered.add(i)
                }
            }
            _filterData.postValue(filtered)

        }

    }

    fun isAppUninstalled(appInfo: AppInfo) {
        Log.v(TAG, appInfo.appName)
        repository.checkForDeletion()
            .subscribe(object : Observer<Boolean> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(t: Boolean) {
                    if (t) {
                        list.remove(appInfo)
                        _filterData.postValue(list)

                    }
                }

                override fun onError(e: Throwable) {
                    appDeletedData.postValue(false)
                }

            })
    }








}