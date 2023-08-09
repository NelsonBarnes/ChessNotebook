package com.nelson.chess_notebook

const val COORDS_PER_VERTEX = 3
const val SQUARE_SIZE = .24

const val vertexShaderCode =
    "uniform mat4 uMVPMatrix;" +
            "attribute vec4 vPosition;" +
            "void main() {" +
            "  gl_Position = uMVPMatrix * vPosition;" +
            "}"

const val fragmentShaderCode =
    "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
            "  gl_FragColor = vColor;" +
            "}"

