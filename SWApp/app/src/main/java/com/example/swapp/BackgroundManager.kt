package com.example.swapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.util.Log
import java.lang.Exception

class BackgroundManager(application: Application) : Application.ActivityLifecycleCallbacks {
    interface Listener{
        fun onBecameForeground()
        fun onBecameBackground()
    }
    var sInstance : BackgroundManager? = null

    private val listeners = ArrayList<BackgroundManager.Listener>()
    fun registerListener(listener: BackgroundManager.Listener?) {
        listeners.add(listener!!)
    }
    fun unregisterListener(listener: BackgroundManager.Listener?) {
        listeners.remove(listener!!)
    }

    var isInBackground = true

    var mBackgroundTransition : Runnable? = null
    var mBackgroundDelayHandler = Handler()

    fun notifyOnBecomeBackground(){
        for (listener in listeners){
            try{
                listener.onBecameBackground()
            } catch (e : Exception){
                Log.e(LOG, "listener threw exception + " + e)
            }
        }
    }
    fun notifyOnBecomeForeground(){
        for (listener in listeners){
            try{
                listener.onBecameForeground()
            } catch (e : Exception){
                Log.e(LOG, "listener threw exception + " + e)
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (!isInBackground && mBackgroundTransition == null){
            mBackgroundTransition = Runnable { ->
                isInBackground = true
                mBackgroundTransition = null
                notifyOnBecomeBackground()
                Log.i(LOG, "app is bg")
            }
            mBackgroundDelayHandler.postDelayed(mBackgroundTransition, BACKGROUND_DELAY)
        }
    }
    override fun onActivityResumed(activity: Activity) {
        if (mBackgroundTransition != null){
            mBackgroundDelayHandler.removeCallbacks(mBackgroundTransition)
            mBackgroundTransition = null
        }
        if (isInBackground){
            isInBackground = false
            notifyOnBecomeForeground()
            Log.i(LOG, "app is fg")
        }
    }

    override fun onActivityStopped(activity: Activity) {}
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {}

    companion object {
        private val LOG =
            BackgroundManager::class.java.simpleName
        const val BACKGROUND_DELAY: Long = 500
        private var sInstance: BackgroundManager? = null
        operator fun get(application: Application): BackgroundManager {
            if (sInstance == null) {
                sInstance = BackgroundManager(application)
            }
            return sInstance!!
        }
    }
}