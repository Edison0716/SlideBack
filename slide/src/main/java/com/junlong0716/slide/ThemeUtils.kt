package com.junlong0716.slide

import android.app.Activity
import android.app.ActivityOptions
import android.util.Log
import java.lang.Exception

/**
 * @ClassName:      ThemeUtils
 * @Description:    目的是调用 #convertToTranslucent(android.app.Activity.TranslucentConversionListener,ActivityOptions)
 * @Author:         LiJunlong
 * @CreateDate:     2020/5/1 1:19 PM
 */
object ThemeUtils {
    fun setActivityTranslucentTheme(activity: Activity) {
        try {
            // 反射获取ActivityOptions
            val getActivityOptions = Activity::class.java.getDeclaredMethod("getActivityOptions")
            getActivityOptions.isAccessible = true
            val options = getActivityOptions.invoke(activity)

            val declaredClasses = Activity::class.java.declaredClasses
            var listenerClass: Class<*>? = null
            for (clazz in declaredClasses) {
                if (clazz.simpleName.contains("TranslucentConversionListener")) {
                    listenerClass = clazz
                    break
                }
            }
            val convertToTran = Activity::class.java.getDeclaredMethod("convertToTranslucent", listenerClass, ActivityOptions::class.java)
            convertToTran.isAccessible = true
            convertToTran.invoke(activity, null, options)
        } catch (e: Exception) {
            Log.e("SET-THEME-EXCEPTION", e.message.toString())
        }
    }
}