package com.example.kotlin_messaging_example

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_reactions.*


class ReactionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reactions)

        if (supportActionBar != null)
            supportActionBar?.hide()

        val width = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            200F,
            resources.displayMetrics
        ).toInt()
        val height = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            50F,
            resources.displayMetrics
        ).toInt()

        window.setLayout(width, height)

        val extras = intent.extras
        if (extras != null) {

            var params = window.attributes
            params.gravity = Gravity.TOP
            params.x = extras.getInt("x")
            params.y = extras.getInt("y")
            window.attributes = params

            /*
            var params2 = reaction_main.layoutParams as FrameLayout.LayoutParams
            params2.topMargin = extras.getInt("y")
            params2.leftMargin = extras.getInt("x")
            reaction_main.layoutParams = params2
            */


            //reaction_main.x = extras.getInt("x").toFloat()
            //reaction_main.y = extras.getInt("y").toFloat()

        }

        val ring: MediaPlayer = MediaPlayer.create(applicationContext, R.raw.rise)
        ring.start()


        for (index in 0 until (reaction_layout as ViewGroup).childCount) {
            val nextChild = (reaction_layout as ViewGroup).getChildAt(index)
            nextChild.setOnTouchListener(fun(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        v.scaleX = 1.5F
                        v.scaleY = 1.5F
                    }
                    MotionEvent.ACTION_UP -> {
                        v.scaleX = 1.0F
                        v.scaleY = 1.0F
                        val returnIntent = Intent()
                        returnIntent.putExtra("emoji", v.tag.toString())
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                    MotionEvent.ACTION_CANCEL -> {
                        v.scaleX = 1.0F
                        v.scaleY = 1.0F
                    }
                }
                return true
            })
        }
    }
}