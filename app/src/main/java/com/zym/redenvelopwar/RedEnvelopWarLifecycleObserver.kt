package com.zym.redenvelopwar

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData

/**
 * 新红包雨预热数据
 */
var mPreheatRedRaperBean: SRedRaperBean? = null

/**
 * 已初始化新红包雨
 */
var mInitRedEnvelopWar = false

const val PACK_OUT_EVENT = "red_envelop_war_pack_out_event"
const val ON_REMOVE_RED_ENVELOP_WAR = "on_remove_red_envelop_war"

class RedEnvelopWarLifecycleObserver : LifecycleObserver {

    val mRedRaperCountdownLiveData = MutableLiveData<SRedRaperBean>()
    val mRedRaperStartLiveData = MutableLiveData<SRedRaperBean>()
    val mRedRaperSettlementLiveData = MutableLiveData<RedRaperSettlementBean>()
}