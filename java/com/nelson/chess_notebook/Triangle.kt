package com.nelson.chess_notebook

import android.opengl.GLES30
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

var triangleCoords = floatArrayOf(
    0.0f, 1.0f, 0.0f,
    -1.0f, -1.0f, 0.0f,
    1.0f,  -1.0f, 0.0f
)

class Triangle {
    // Use to access and set the view transformation
    private var vPMatrixHandle: Int = 0

    private val color = floatArrayOf(0.6f, 0.8f, 0.2f, 1.0f)

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply{
                put(triangleCoords)
                position(0)
            }
        }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    private var mProgram: Int

    init {
        val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES30.glCreateProgram().also {

            // add the vertex shader to program
            GLES30.glAttachShader(it, vertexShader)

            // add the fragment shader to program
            GLES30.glAttachShader(it, fragmentShader)

            // creates OpenGL ES program executables
            GLES30.glLinkProgram(it)
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        // Add program to OpenGL ES environment
        GLES30.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
        positionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition").also {

            // Enable a handle to the triangle vertices
            GLES30.glEnableVertexAttribArray(it)

            // Prepare the triangle coordinate data
            GLES30.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES30.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )

            // get handle to fragment shader's vColor member
            mColorHandle = GLES30.glGetUniformLocation(mProgram, "vColor").also { colorHandle ->

                // Set color for drawing the triangle
                GLES30.glUniform4fv(colorHandle, 1, color, 0)
            }

            // get handle to shape's transformation matrix
            vPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")

            // Pass the projection and view transformation to the shader
            GLES30.glUniformMatrix4fv(vPMatrixHandle, 1, false, mvpMatrix, 0)

            // Draw the triangle
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

            // Disable vertex array
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