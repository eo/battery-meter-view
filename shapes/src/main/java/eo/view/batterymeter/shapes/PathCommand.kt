package eo.view.batterymeter.shapes

import java.math.BigDecimal

class PathCommand(val command: Char, vararg val parameters: BigDecimal) {
    override fun toString(): String {
        return "$command ${parameters.joinToString(" ")}"
    }
}