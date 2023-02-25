package com.zym.redenvelopwar

data class SRedRaperBean(
    val actual_preheat_time: Long,
    val conf_id: Int, // 红包雨id
    val conf_name: String, // 红包雨名称
    val conf_pic: String, // 定时红包雨头像
    val continue_time: Int,
    val end_countdown: Int, // 全屏特效倒计时
    val exec_time: Int,
    val preheat_time: Int,
    val start_countdown: Int, // 右下角预热倒计时
    val type: Int, // 1：定时触发，2：活动触发
    val uid: Int, // 触发活动红包雨的用户id
    val avatar: String, // 活动红包雨头像
    val nick_name: String, // 活动红包雨用户昵称
    val stage:Int,//当前阶段，0：无红包雨，1：倒计时，2：进行中
    val room_ids:List<String>,//满足红包雨的房间id（进房会判断，不满足时stage=0）
    val actual_preheat_time_end_sec: Long, // 红包雨预热结束时间戳
    var actual_red_rain_time_end_sec: Long // 红包雨结束时间戳
)