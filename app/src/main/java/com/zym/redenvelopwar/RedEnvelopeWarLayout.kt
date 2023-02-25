package com.zym.redenvelopwar

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.layout_red_envelope_war.view.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.Exception
import java.util.concurrent.TimeUnit
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author 张雨民
 * @Description: 红包雨
 * @CreateDate: 2022/10/24 11:21
 */
class RedEnvelopeWarLayout(context: Context, attributeSet: AttributeSet) : FrameLayout(context, attributeSet) {

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_red_envelope_war, this)
        EventBus.getDefault().register(this)
    }
    var startBP:()->Unit={}
    var closeBP:()->Unit={}


    companion object {
        const val ENVELOP_PERIOD = 200L
        const val SCALE_FACTOR = 0.4f
        const val ENVELOP_MAX_WIDTH = 76f
        const val ENVELOP_MAX_HEIGHT = 90f
        const val ENVELOP_ANGLE = 75
        const val ENVELOP_DURATION = 3000L

        const val SHOOTING_STAR_PERIOD = 100L
        const val SHOOTING_STAR_WIDTH = 24f
        const val SHOOTING_STAR_HEIGHT = 81f
        const val SHOOTING_STAR_ANGLE = 75
        const val SHOOTING_STAR_DURATION = 3000L

        const val TYPE_TIMING = 1 // 定时红包雨
        const val TYPE_ACTIVITY = 2 // 活动红包雨

        const val PACK_UP_DURATION = 1000L
    }

    private val mScreenWidth = ScreenUtils.getScreenWidth()
    private val mEnvelopDistance = SizeUtils.dp2px(600f)
    private val mShootingStarDistance = SizeUtils.dp2px(600f)

    private var mEnvelopDisposable : Disposable? = null
    private var mShootingStarDisposable : Disposable? = null
    private var mTimer : CountDownTimer? = null

    private var firstClick = true
    private var mRoomId = ""
    private var mTargetView: View? = null

    fun start(/*redRaperBean: SRedRaperBean, */roomId: String, targetView: View?) {
//        val endTimeStamp = redRaperBean.actual_red_rain_time_end_sec
//        val currTimeStamp = System.currentTimeMillis()
//        if (currTimeStamp >= endTimeStamp) {
//            EventBus.getDefault().post(ON_REMOVE_RED_ENVELOP_WAR)
//            return
//        }

        mRoomId = roomId
        mTargetView = targetView
        visibility = View.VISIBLE
        renderDynamicEffect()
        renderTips()
        startBP()
    }

    private fun renderDynamicEffect() {
        if (mEnvelopDisposable == null) {
            mEnvelopDisposable = Flowable.interval(0, ENVELOP_PERIOD, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    startEnvelopAnimator()
                }
        }

        if (mShootingStarDisposable == null) {
            mShootingStarDisposable = Flowable.interval(0, SHOOTING_STAR_PERIOD, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread()).subscribe {
                    startShootingStarAnimator()
                }
        }

//        SVGAUtils.playSVGA(
//            context,
//            bottomAnimRedEnvelopWar,
//            "bottom_anim_red_envelop_war.svga",
//            true,
//            null
//        )
    }

    private fun renderTips() {
//        var avatarUrl = ""
//        var nickname = ""
//        if (redRaperBean.type == TYPE_TIMING) {
//            avatarUrl = redRaperBean.conf_pic
//            nicknameRedEnvelopWar.visibility = View.INVISIBLE
//        } else if (redRaperBean.type == TYPE_ACTIVITY) {
//            avatarUrl = redRaperBean.avatar
//            nickname = redRaperBean.nick_name
//            nicknameRedEnvelopWar.visibility = View.VISIBLE
//        }
////        DFImage.getInstance().display(avatarRedEnvelopWar, avatarUrl)
//        nicknameRedEnvelopWar.text = nickname
//        marqueeRedEnvelopWar.text = redRaperBean.conf_name
//        marqueeRedEnvelopWar.isSelected = true
//
//        val endTimeStamp = redRaperBean.actual_red_rain_time_end_sec
//        val currTimeStamp = System.currentTimeMillis()
        mTimer = object : CountDownTimer(10 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                cdRedEnvelopWar.text = "${millisUntilFinished / 1000}s"
            }

            override fun onFinish() {
                EventBus.getDefault().post(ON_REMOVE_RED_ENVELOP_WAR)
            }

        }
        mTimer?.start()

        closeRedEnvelopWar.setOnClickListener {
            packInto()
            closeBP()
        }
    }

    private fun startEnvelopAnimator() {
        val envelopView = buildEnvelopView()

        val factorX = Math.random()
        val startX = mScreenWidth * factorX - mScreenWidth / 4
        val startY = headerRedEnvelopeWar.bottom / 2
        val endX = mEnvelopDistance * cos(Math.toRadians(ENVELOP_ANGLE.toDouble())) + startX
        val endY = mEnvelopDistance * sin(Math.toRadians(ENVELOP_ANGLE.toDouble())) + startY

        val translationX = ObjectAnimator.ofFloat(envelopView, "translationX", startX.toFloat(), endX.toFloat())
        val translationY = ObjectAnimator.ofFloat(envelopView, "translationY", startY.toFloat(), endY.toFloat())
        val alpha = ObjectAnimator.ofFloat(envelopView, "alpha", 0f, 1f, 1f, 1f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(alpha, translationX, translationY)
        animatorSet.duration = ENVELOP_DURATION
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                if (ViewCompat.isAttachedToWindow(envelopView)) {
                    removeView(envelopView)
                }
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }


        })

        addView(envelopView)
        animatorSet.start()
    }

    private fun buildEnvelopView() : ImageView {
        val imageView = ImageView(context)
//        val minScale = 1f - SCALE_FACTOR
//        val maxScale = 1f + SCALE_FACTOR
//        val scale = minScale + Math.random() * (maxScale - minScale) + 1
        val scale = 1
        val width = SizeUtils.dp2px(ENVELOP_MAX_WIDTH) * scale.toInt()
        val height = SizeUtils.dp2px(ENVELOP_MAX_HEIGHT) * scale.toInt()
        val layoutParams = ViewGroup.LayoutParams(width, height)
        imageView.layoutParams = layoutParams
        imageView.setImageResource(R.mipmap.icon_red_envelop)
        imageView.setOnClickListener {
            it.visibility = View.GONE
            if (firstClick) {
                firstClick = false

                CoroutinesUtils.coroutinesScope.launch(Dispatchers.IO) {
                    try {
                        val map = HashMap<String, String>()
                        map["token"] = "jdkfjkidfu898fdkjds"
                        map["room_id"] = mRoomId
                        // pick
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return imageView
    }

    private fun startShootingStarAnimator() {
        val shootingStarView = buildShootingStarView()

        val factorX = Math.random()
        val startX = mScreenWidth * factorX - mScreenWidth / 4
        val startY = headerRedEnvelopeWar.bottom / 2
        val endX = mShootingStarDistance * cos(Math.toRadians(ENVELOP_ANGLE.toDouble())) + startX
        val endY = mShootingStarDistance * sin(Math.toRadians(ENVELOP_ANGLE.toDouble())) + startY

        val translationX = ObjectAnimator.ofFloat(shootingStarView, "translationX", startX.toFloat(), endX.toFloat())
        val translationY = ObjectAnimator.ofFloat(shootingStarView, "translationY", startY.toFloat(), endY.toFloat())
        val alpha = ObjectAnimator.ofFloat(shootingStarView, "alpha", 0f, 1f, 0f)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(alpha, translationX, translationY)
        animatorSet.duration = SHOOTING_STAR_DURATION

        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator) {
            }

            override fun onAnimationEnd(p0: Animator) {
                if (ViewCompat.isAttachedToWindow(shootingStarView)) {
                    removeView(shootingStarView)
                }
            }

            override fun onAnimationCancel(p0: Animator) {
            }

            override fun onAnimationRepeat(p0: Animator) {
            }


        })

        addView(shootingStarView)
        animatorSet.start()
    }

    private fun buildShootingStarView() : ImageView {
        val imageView = ImageView(context)
        val width = SizeUtils.dp2px(SHOOTING_STAR_WIDTH)
        val height = SizeUtils.dp2px(SHOOTING_STAR_HEIGHT)
        val layoutParams = ViewGroup.LayoutParams(width, height)
        imageView.layoutParams = layoutParams
        imageView.setImageResource(R.mipmap.icon_shooting_star)
        return imageView
    }

    fun end() {
        visibility = View.GONE
        mTimer?.cancel()
        cancelEnvelopInterval()
        cancelShootingStarInterval()
        firstClick = true
    }

    /**
     * 收起红包雨
     */
    private fun packInto() {
        cancelEnvelopInterval()
        cancelShootingStarInterval()

        val scaleAnimation = ScaleAnimation(
            1f,
            0f,
            1f,
            0f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scaleAnimation.duration = PACK_UP_DURATION

        val fromXValue = 0f
        val fromYValue = 0f
        var toXValue: Int
        var toYValue: Int
        val location = IntArray(2)
        mTargetView?.getLocationOnScreen(location)
        location.let {
            toXValue = it[0] - headerRedEnvelopeWar.width / 2 + (mTargetView?.width?.div(2) ?: 0)
            toYValue = it[1] - headerRedEnvelopeWar.height / 2 + (mTargetView?.height?.div(2) ?: 0)
        }
        val translateAnimation = TranslateAnimation(
            Animation.ABSOLUTE,
            fromXValue,
            Animation.ABSOLUTE,
            toXValue.toFloat(),
            Animation.ABSOLUTE,
            fromYValue,
            Animation.ABSOLUTE,
            toYValue.toFloat()
        )
        translateAnimation.duration = PACK_UP_DURATION

        val animationSet = AnimationSet(true)
        animationSet.addAnimation(scaleAnimation)
        animationSet.addAnimation(translateAnimation)
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

        })
        rectHeaderRedEnvelopWar.startAnimation(animationSet)
    }

    /**
     * 恢复红包雨
     */
    private fun packOut() {
        visibility = View.VISIBLE
        renderDynamicEffect()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        cancelEnvelopInterval()
        cancelShootingStarInterval()
        mTimer?.cancel()
        EventBus.getDefault().unregister(this)
    }

    private fun cancelShootingStarInterval() {
        if (mShootingStarDisposable != null && mShootingStarDisposable?.isDisposed == false) {
            mShootingStarDisposable?.dispose()
            mShootingStarDisposable = null
        }
    }

    private fun cancelEnvelopInterval() {
        if (mEnvelopDisposable != null && mEnvelopDisposable?.isDisposed == false) {
            mEnvelopDisposable?.dispose()
            mEnvelopDisposable = null
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onLiveEvent(event: String) {
        if (event == PACK_OUT_EVENT) {
            packOut()
            startBP()
        }
    }
}