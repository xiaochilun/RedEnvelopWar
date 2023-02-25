package com.zym.redenvelopwar

data class RedRaperSettlementBean(
    val avatar: String,
    val conf_id: Int,
    val conf_name: String,
    val conf_pic: String,
    val continue_time: Int,
    val exec_time: Int,
    val follow_status: Int,////与触发人关注关系，1：互相均未关注，2：A关注了B，但B未关注A，3：A未关注B，但B关注了A，4：已互相关注
    val is_win: Int,
    val nick_name: String,
    val preheat_time: Int,
    val prize_alias: String,
    val type: Int,//1：定时触发，2：活动触发
    val uid: Int,
    val win_list: List<Win>,
    val win_num: Int,
    var isChatRoom:Boolean
)

data class Win(
    val avatar: String,
    val nickname: String,
    val uid: Int,
    val win_num:Int,
    val prize_alias:String
)