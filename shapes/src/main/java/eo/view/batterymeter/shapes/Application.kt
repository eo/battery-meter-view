package eo.view.batterymeter.shapes

import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Paths


val OUTPUT_DIR: File = Paths.get("..", "batterymeter", "src", "main", "res", "raw").toFile()
const val OUTPUT_FILE_PREFIX = "battery_shapes"

const val SVG_FILES_DIR = "svg"
val themes = listOf("sharp", "rounded")
val shapes = listOf("battery_shape", "alert_indicator", "charging_indicator", "unknown_indicator")

fun main(args: Array<String>) {

    themes.forEach { theme ->
        val outputFileName = "${OUTPUT_FILE_PREFIX}_$theme"
        val shapeNames = shapes.map { shape -> "${shape}_$theme" }
        var aspectRatio = 0f

        val pathCommandsOfAllShapes = shapeNames.map { shapeName ->
            val filePath = "$SVG_FILES_DIR${File.separator}$shapeName.svg"
            val svgDocument = loadSvgDocument(filePath)
            val svgElement = svgDocument.rootElement
            val viewBox = extractViewBox(svgElement)
            val pathData = extractPathData(svgElement)
            val pathCommands = parsePathData(pathData)

            aspectRatio = viewBox.width / viewBox.height

            scaledPathCommands(pathCommands, viewBox)
        }

        writeOutputFileForThemeShapes(outputFileName, aspectRatio, pathCommandsOfAllShapes)

        pathCommandsOfAllShapes.forEach { pathCommands ->
            pathCommands.forEach { println(it) }
            println()
        }
    }
}

fun writeOutputFileForThemeShapes(
    outputFileName: String,
    aspectRatio: Float,
    pathCommandsOfAllShapes: List<List<PathCommand>>
) {
    OUTPUT_DIR.mkdirs()

    DataOutputStream(FileOutputStream(File(OUTPUT_DIR, outputFileName))).use { out ->
        out.writeFloat(aspectRatio)

        pathCommandsOfAllShapes.forEach { pathCommands ->
            val shapeBytes = pathCommands.toByteArray()

            out.writeInt(shapeBytes.size)
            out.write(shapeBytes)
        }
    }
}

fun List<PathCommand>.toByteArray(): ByteArray {
    val byteStream = ByteArrayOutputStream()

    DataOutputStream(byteStream).use { out ->
        forEach { pathCommand ->
            out.writeChar(pathCommand.command.toInt())
            pathCommand.parameters.forEach {
                out.writeFloat(it)
            }
        }
    }

    return byteStream.toByteArray()
}
