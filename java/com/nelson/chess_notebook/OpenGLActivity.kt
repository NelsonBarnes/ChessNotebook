package com.nelson.chess_notebook

import android.os.Bundle
import android.app.Activity
import android.opengl.GLSurfaceView

class OpenGLActivity : Activity() {
    private lateinit var gLView: GLSurfaceView

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create a GLSurfaceView instance and set it as the ContentView for this Activity.
        gLView = OpenGlSurfaceView(this)
        setContentView(gLView)
    }
}
