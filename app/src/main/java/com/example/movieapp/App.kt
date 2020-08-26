package com.example.movieapp

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.movieapp.provider.resolver.ResolverHandler
import io.realm.Realm


/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
private const val TAG = "App"

class App : Application() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        getAllAppLetBuildApp(this).map {
            Log.d(
                TAG,
                "app with your permissions : ${it.packageName}"
            )

        }

    }

    fun getAllAppLetBuildApp(context: Context) =
        context.packageManager.getInstalledPackages(PackageManager.GET_PERMISSIONS)
            .filter { packageInfo ->
                packageInfo.permissions
                    ?.any { it -> it.name == context.getString(R.string.permisionName) }
                    ?: false
            }
}