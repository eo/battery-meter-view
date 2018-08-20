package eo.view.batterymeter.shapes

import java.io.File


const val SVG_DIR = "svg"
val shapes = listOf("battery_shape", "charging_indicator", "alert_indicator")
val themes = listOf("sharp", "rounded")

fun main(args: Array<String>) {
    val svgFileNames = shapes.flatMap { shape ->
        themes.map { theme ->
            "${shape}_$theme.svg"
        }
    }

    svgFileNames.forEach { fileName ->
        val filePath = "$SVG_DIR${File.separator}$fileName"
        val svgDocument = loadSvgDocument(filePath)
        val svgElement = svgDocument.rootElement
        val viewBox = extractViewBox(svgElement)
        val pathData = extractPathData(svgElement)

        parsePathData(pathData)
    }
}
