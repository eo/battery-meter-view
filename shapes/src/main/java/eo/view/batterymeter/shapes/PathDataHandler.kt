package eo.view.batterymeter.shapes

import org.apache.batik.parser.PathHandler
import java.math.BigDecimal

/**
 * Handles path data parsing and generates a list of path commands.
 * Converts relative commands to matching absolute commands.
 * Converts unsupported commands (e.g. S) on Android to supported ones (e.g. C).
 * Converts H and V commands to L commands.
 */
class PathDataHandler : PathHandler {

    var pathCommands = emptyList<PathCommand>()
        private set

    private val commandList = mutableListOf<PathCommand>()

    private var lastX = BigDecimal.ZERO
    private var lastY = BigDecimal.ZERO
    private var lastX1 = BigDecimal.ZERO
    private var lastY1 = BigDecimal.ZERO

    override fun startPath() {
        commandList.clear()

        lastX = BigDecimal.ZERO
        lastY = BigDecimal.ZERO
        lastX1 = BigDecimal.ZERO
        lastY1 = BigDecimal.ZERO
    }

    override fun endPath() {
        pathCommands = commandList.toList()
    }

    override fun arcAbs(
        rx: Float,
        ry: Float,
        xAxisRotation: Float,
        largeArcFlag: Boolean,
        sweepFlag: Boolean,
        x: Float,
        y: Float
    ) {
        println("A $rx $ry $xAxisRotation $largeArcFlag $sweepFlag $x $y")
        throw NotImplementedError("A (arc) command is not implemented!")
    }

    override fun arcRel(
        rx: Float,
        ry: Float,
        xAxisRotation: Float,
        largeArcFlag: Boolean,
        sweepFlag: Boolean,
        x: Float,
        y: Float
    ) {
        println("a $rx $ry $xAxisRotation $largeArcFlag $sweepFlag $x $y")
        throw NotImplementedError("a (arc) command is not implemented!")
    }

    override fun closePath() {
        addCommand('Z')
    }

    override fun curvetoCubicAbs(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float) {
        lastX1 = x2.toBigDecimal()
        lastY1 = y2.toBigDecimal()
        lastX = x.toBigDecimal()
        lastY = y.toBigDecimal()

        addCommand('C', x1.toBigDecimal(), y1.toBigDecimal(), lastX1, lastY1, lastX, lastY)
    }

    override fun curvetoCubicRel(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float) {
        val absX1 = x1.toBigDecimal() + lastX
        val absY1 = y1.toBigDecimal() + lastY

        lastX1 = x2.toBigDecimal() + lastX
        lastY1 = y2.toBigDecimal() + lastY

        lastX += x.toBigDecimal()
        lastY += y.toBigDecimal()


        addCommand('C', absX1, absY1, lastX1, lastY1, lastX, lastY)
    }

    override fun curvetoCubicSmoothAbs(x2: Float, y2: Float, x: Float, y: Float) {
        val x1 = 2.toBigDecimal() * lastX - lastX1
        val y1 = 2.toBigDecimal() * lastY - lastY1

        lastX1 = x2.toBigDecimal()
        lastY1 = y2.toBigDecimal()
        lastX = x.toBigDecimal()
        lastY = y.toBigDecimal()

        addCommand('C', x1, y1, lastX1, lastY1, lastX, lastY)
    }

    override fun curvetoCubicSmoothRel(x2: Float, y2: Float, x: Float, y: Float) {
        val x1 = 2.toBigDecimal() * lastX - lastX1
        val y1 = 2.toBigDecimal() * lastY - lastY1

        lastX1 = x2.toBigDecimal() + lastX
        lastY1 = y2.toBigDecimal() + lastY

        lastX += x.toBigDecimal()
        lastY += y.toBigDecimal()

        addCommand('C', x1, y1, lastX1, lastY1, lastX, lastY)
    }

    override fun curvetoQuadraticAbs(x1: Float, y1: Float, x: Float, y: Float) {
        println("Q $x1 $y1 $x $y")
        throw NotImplementedError("Q (quadratic Bezier) command is not implemented!")
    }

    override fun curvetoQuadraticRel(x1: Float, y1: Float, x: Float, y: Float) {
        println("q $x1 $y1 $x $y")
        throw NotImplementedError("q (quadratic Bezier) command is not implemented!")
    }

    override fun curvetoQuadraticSmoothAbs(x: Float, y: Float) {
        println("T $x $y")
        throw NotImplementedError("T (smooth quadratic Bezier) command is not implemented!")
    }

    override fun curvetoQuadraticSmoothRel(x: Float, y: Float) {
        println("t $x $y")
        throw NotImplementedError("t (smooth quadratic Bezier) command is not implemented!")
    }

    override fun linetoAbs(x: Float, y: Float) {
        lastX = x.toBigDecimal()
        lastY = y.toBigDecimal()

        addCommand('L', lastX, lastY)
    }

    override fun linetoRel(x: Float, y: Float) {
        lastX += x.toBigDecimal()
        lastY += y.toBigDecimal()

        addCommand('L', lastX, lastY)
    }

    override fun linetoHorizontalAbs(x: Float) {
        lastX = x.toBigDecimal()

        addCommand('L', lastX, lastY)
    }

    override fun linetoHorizontalRel(x: Float) {
        lastX += x.toBigDecimal()

        addCommand('L', lastX, lastY)
    }

    override fun linetoVerticalAbs(y: Float) {
        lastY = y.toBigDecimal()

        addCommand('L', lastX, lastY)
    }

    override fun linetoVerticalRel(y: Float) {
        lastY += y.toBigDecimal()

        addCommand('L', lastX, lastY)
    }

    override fun movetoAbs(x: Float, y: Float) {
        lastX = x.toBigDecimal()
        lastY = y.toBigDecimal()

        addCommand('M', lastX, lastY)
    }

    override fun movetoRel(x: Float, y: Float) {
        lastX += x.toBigDecimal()
        lastY += y.toBigDecimal()

        addCommand('M', lastX, lastY)
    }

    private fun addCommand(command: Char, vararg parameters: BigDecimal) {
        commandList.add(PathCommand(command, *parameters))
    }

}