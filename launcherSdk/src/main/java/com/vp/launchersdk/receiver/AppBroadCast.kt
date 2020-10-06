package com.vp.launchersdk.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log

import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers


/**
 * Created by Vishwanath Patil on 02/10/20.
 */

fun notifyForAddition(): Observable<Boolean> {
    Log.v("AppBroadCast", "true")


    val o: Observable<Boolean> = Observable.create { emitter ->
        emitter.onNext(true)
        emitter.onComplete()
    }

    return o.observeOn(AndroidSchedulers.mainThread())


}

fun notifyForDeletion(source: String): Observable<Boolean> {


    val a: Observable<Boolean> = if (source.equals("broadcast", ignoreCase = true)) {
        Log.v("AppBroadCast", "Register for Single Observer $source")
        Observable.create{
            emitter -> emitter.onNext(true)
        }
    } else {
        Log.v("AppBroadCast", "Register for Single Observer $source")
        Observable.create{
                emitter -> emitter.onNext(false)
        }
    }


    return a


}

fun registerBroadCast(context: Context?) {
    val intentFilter = IntentFilter()
    intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
    intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
    intentFilter.addDataScheme("package")
    context?.registerReceiver(BroadCastListener(), intentFilter)

}

fun unregisterBroadCast(context: Context?) {
    context?.unregisterReceiver(BroadCastListener())
}


class BroadCastListener : BroadcastReceiver() {
    override fun onReceive(context: Context?, newIntent: Intent?) {

        newIntent?.let { intent ->
            intent.action?.let {
                when (it) {


                    Intent.ACTION_PACKAGE_ADDED -> {
                        Toast.makeText(
                            context?.applicationContext,
                            "Application is added",
                            Toast.LENGTH_LONG
                        ).show()

                        Log.v("AppBroadcast",newIntent.`package`.toString())

                       // notifyForAddition()

                    }
                    Intent.ACTION_PACKAGE_REMOVED -> {
                        //  Toast.makeText(context?.applicationContext,"Application is Uninstalled",Toast.LENGTH_LONG).show()
                        Log.v("AppBroadCast", "ACTION_PACKAGE_REMOVED")
                      //  notifyForDeletion("broadcast")
                      //  context?.let { innerContext -> MyPackageManager(innerContext).getListOfApps() }


                    }
                    else -> {
                    }
                }
            }
        }


    }


}