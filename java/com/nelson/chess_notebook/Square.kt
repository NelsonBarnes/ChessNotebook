package com.nelson.chess_notebook

import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class Square(
    private val x: Float, private val y: Float,
    private val color: FloatArray, val san: String) {

    private val drawOrder = shortArrayOf(0, 1, 2, 0, 2, 3) // order to draw vertices
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex
    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0
    private var vPMatrixHandle: Int = 0
    private var mProgram: Int

    private val squareCoords = floatArrayOf(
        x, y, 0f, // top left
        x, (y - SQUARE_SIZE).toFloat(), 0f, // bottom left
        (x + SQUARE_SIZE).toFloat(), (y - SQUARE_SIZE).toFloat(), 0f, // bottom right
        (x + SQUARE_SIZE).toFloat(), y, 0f // top right
    )

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(squareCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply{
                put(squareCoords)
                position(0)
            }
        }

    private val drawListBuffer: ShortBuffer =
        // (# of coordinate values * 2 bytes per short)
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

    init {
        val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram = GLES30.glCreateProgram().also {
            GLES30.glAttachShader(it, vertexShader)
            GLES30.glAttachShader(it, fragmentShader)
            GLES30.glLinkProgram(it) // creates OpenGL ES program executables
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES30.glUseProgram(mProgram)

        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition").also {
            GLES30.glEnableVertexAttribArray(it) // Enable a handle to the vertices
            GLES30.glVertexAttribPointer( // Prepare the coordinate data
                it,
                COORDS_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->
                GLES30.glUniform4fv(colorHandle, 1, color, 0)
            }

            vPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix") // get handle to shape's transformation matrix

            GLES30.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0) // Pass the projection and view transformation to the shader

            GLES30.glDrawElements(
                GLES30.GL_TRIANGLES, drawOrder.size,
                GLES30.GL_UNSIGNED_SHORT, drawListBuffer)

            GLES30.glDisableVertexAttribArray(positionHandle)
        }
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES30.glCreateShader(type).also { shader ->
            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)
        }
    }
}