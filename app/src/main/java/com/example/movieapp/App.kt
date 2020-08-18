package com.example.movieapp

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Log
import io.realm.Realm


/**
 * Created by Benjamin Vouillon on 15,July,2020
 */
private const val TAG = "App"

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        val p = getPackageManager();
        val packagesWithTheSamePermission = mutableListOf<PackageInfo>()
        p.getInstalledPackages(PackageManager.GET_PERMISSIONS).map { packageInfo ->
            packageInfo.permissions?.map { it ->
                if (it.name == "com.example.movieapp.permission.ACCES_DATABASE")
                    packagesWithTheSamePermission.add(packageInfo)
            }
        }

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