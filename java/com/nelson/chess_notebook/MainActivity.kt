package com.nelson.chess_notebook

import android.content.ClipData
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.view.View.OnTouchListener
import androidx.activity.ComponentActivity


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        val buttonClick = findViewById<Button>(R.id.button_click)
        buttonClick.setOnClickListener {
            val intent = Intent(this, OpenGLActivity::class.java)
            startActivity(intent)
        }
        */
    }

    private class MyTouchListener : OnTouchListener {
        override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
            return if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                val data = ClipData.newPlainText("", "")
                val shadowBuilder = DragShadowBuilder(
                    view
                )
                view.startDragAndDrop(data, shadowBuilder, view, 0)
                view.visibility = View.INVISIBLE
                true
            } else {
                false
            }
        }
    }
}