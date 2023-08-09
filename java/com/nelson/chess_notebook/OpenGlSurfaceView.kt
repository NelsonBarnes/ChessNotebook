package com.nelson.chess_notebook

import android.content.Context
import android.opengl.GLSurfaceView

class OpenGlSurfaceView(context: Context) : GLSurfaceView(context) {

    private val renderer: OpenGlRenderer

    init {
        // Create an OpenGL ES 3.0 context
        setEGLContextClientVersion(3)

        renderer = OpenGlRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}
