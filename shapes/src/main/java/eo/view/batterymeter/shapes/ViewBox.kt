package eo.view.batterymeter.shapes

data class ViewBox(val minX: Float, val minY: Float, val width: Float, val height: Float) {

    companion object {
        fun fromString(str: String): ViewBox {
            val numbers = str.split(' ')
                .map { it.toFloatOrNull() ?: 0f }
                .toFloatArray()

            if (numbers.size != 4) {
                throw IllegalArgumentException("String must have 4 numbers separated by spaces")
            }

            return ViewBox(numbers[0], numbers[1], numbers[2], numbers[3])
        }
    }

}