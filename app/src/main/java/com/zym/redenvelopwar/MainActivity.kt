package com.zym.redenvelopwar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layoutRedEnvelopWar.start("1", targetView)
        targetView.setOnClickListener {
            EventBus.getDefault().post(PACK_OUT_EVENT)
        }
    }
}