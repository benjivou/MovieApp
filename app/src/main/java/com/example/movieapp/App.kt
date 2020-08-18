package com.example.movieapp

import android.app.Application
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
        val appinstall = p.getInstalledPackages(PackageManager.GET_PERMISSIONS)
        val it = appinstall.iterator()
        val listOfPermission = mutableListOf<PackageInfo>()
        while (it.hasNext()) {
            val rf = it.next()
            rf.permissions?.map { it -> if (it.name == "com.example.movieapp.permission.ACCES_DATABASE") listOfPermission.add(rf) }
        }

        listOfPermission.map { Log.d(TAG, "app with your permissions : ${it.packageName}") }

    }
}