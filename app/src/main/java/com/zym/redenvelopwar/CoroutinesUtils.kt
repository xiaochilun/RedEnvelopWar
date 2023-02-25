package com.zym.redenvelopwar

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

object CoroutinesUtils {

    /**
     * 单例创建协程
     */
    val coroutinesScope: CoroutineScope by lazy (mode = LazyThreadSafetyMode.SYNCHRONIZED){ MainScope() }

    /**
     * 取消协程的任务
     */
    fun cancel(){
        coroutinesScope.cancel()
    }
}