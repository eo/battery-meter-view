package eo.view.batterymeter.shapes

data class ViewBox(val minX: Int, val minY: Int, val width: Int, val height: Int) {

    companion object {
        fun fromString(str: String): ViewBox {
            val numbers = str.split(' ')
                .map { it.toIntOrNull() ?: 0 }
                .toIntArray()

            if (numbers.size != 4) {
                throw IllegalArgumentException("String must have 4 numbers separated by spaces")
            }

            return ViewBox(numbers[0], numbers[1], numbers[2], numbers[3])
        }
    }

}