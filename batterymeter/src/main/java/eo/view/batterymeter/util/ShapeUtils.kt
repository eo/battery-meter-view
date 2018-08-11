package eo.view.batterymeter.util

import kotlin.math.max

fun scaledPoints(vararg points: Int): FloatArray {
    val scaledPoints = FloatArray(points.size)

    var maxX = points[0]
    var maxY = points[1]

    for (index in 2..(points.size - 2) step 2) {
        maxX = max(maxX, points[index])
        maxY = max(maxY, points[index + 1])
    }

    for (index in 0..(points.size - 2) step 2) {
        scaledPoints[index] = points[index] / maxX.toFloat()
        scaledPoints[index + 1] = points[index + 1] / maxY.toFloat()
    }

    return scaledPoints
}