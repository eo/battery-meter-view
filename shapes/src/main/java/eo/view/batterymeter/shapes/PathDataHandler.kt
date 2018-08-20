package eo.view.batterymeter.shapes

import org.apache.batik.parser.PathHandler

class PathDataHandler : PathHandler {

    override fun startPath() {
        // TODO: initialize data structures
    }

    override fun endPath() {
        println()
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
    }

    override fun closePath() {
        println("z")
    }

    override fun curvetoCubicAbs(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float) {
        println("C $x1 $y1 $x2 $y2 $x $y")
    }

    override fun curvetoCubicRel(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float) {
        println("c $x1 $y1 $x2 $y2 $x $y")
    }

    override fun curvetoCubicSmoothAbs(x2: Float, y2: Float, x: Float, y: Float) {
        println("S $x2 $y2 $x $y")
    }

    override fun curvetoCubicSmoothRel(x2: Float, y2: Float, x: Float, y: Float) {
        println("s $x2 $y2 $x $y")
    }

    override fun curvetoQuadraticAbs(x1: Float, y1: Float, x: Float, y: Float) {
        println("Q $x1 $y1 $x $y")
    }

    override fun curvetoQuadraticRel(x1: Float, y1: Float, x: Float, y: Float) {
        println("q $x1 $y1 $x $y")
    }

    override fun curvetoQuadraticSmoothAbs(x: Float, y: Float) {
        println("T $x $y")
    }

    override fun curvetoQuadraticSmoothRel(x: Float, y: Float) {
        println("t $x $y")
    }

    override fun linetoAbs(x: Float, y: Float) {
        println("L $x $y")
    }

    override fun linetoRel(x: Float, y: Float) {
        println("l $x $y")
    }

    override fun linetoHorizontalAbs(x: Float) {
        println("H $x")
    }

    override fun linetoHorizontalRel(x: Float) {
        println("h $x")
    }

    override fun linetoVerticalAbs(y: Float) {
        println("V $y")
    }

    override fun linetoVerticalRel(y: Float) {
        println("v $y")
    }

    override fun movetoAbs(x: Float, y: Float) {
        println("M $x $y")
    }

    override fun movetoRel(x: Float, y: Float) {
        println("m $x $y")
    }

}