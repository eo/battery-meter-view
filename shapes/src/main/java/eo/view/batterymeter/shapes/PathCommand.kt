package eo.view.batterymeter.shapes

class PathCommand(val command: Char, vararg val parameters: Float) {
    override fun toString(): String {
        return "$command ${parameters.joinToString(" ")}"
    }
}