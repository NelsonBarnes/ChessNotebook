package com.nelson.chess_notebook

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.util.Log

class OpenGlRenderer : GLSurfaceView.Renderer {
    private lateinit var mSquares: List<Square>

    // vPMatrix is an abbreviation for "Model View Projection Matrix"
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES30.glClearColor(0.0f, 0.5f, 0.5f, 1.0f)

        mSquares = createSquares()
    }

    override fun onDrawFrame(unused: GL10) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT) // Redraw background color

        Matrix.setLookAtM( // Set the camera position (View matrix)
            viewMatrix, 0, 0f, 0f, 3f,
            0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        for (square in mSquares) {
            square.draw(vPMatrix)
        }
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        val sideLength = width - 180
        val ratio: Float = 1f
        GLES30.glViewport(90, 180, sideLength, sideLength)

        // this projection matrix is applied to object coordinates in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    private fun createSquares(): List<Square> {
        var squares = mutableListOf<Square>()

        val alp = listOf("a", "b", "c", "d", "e", "f", "g", "h")
        val num = listOf("1", "2", "3", "4", "5", "6", "7", "8")
        val dark = floatArrayOf(.8f, .5f, .2f, 1f)
        val light = floatArrayOf(0f, 0f, 0f, 1f)

        var x = -.96f
        var y = -.72f

        for ((i, a) in alp.withIndex()) {
            for ((k, n) in num.withIndex()) {
                squares.add(
                    Square((x + (i * SQUARE_SIZE)).toFloat(), (y + (k * SQUARE_SIZE)).toFloat(),
                    if ((i % 2) == (k % 2)) light else dark, a + n))
            }
        }

        return squares.toList()
    }
}
