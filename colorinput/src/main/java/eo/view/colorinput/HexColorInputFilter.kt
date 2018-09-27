package eo.view.colorinput

import android.text.InputFilter
import android.text.Spanned

class HexColorInputFilter : InputFilter {

    companion object {
        private const val PREFIX = '#'
        const val MAX_LENGTH = 9 // # + 8 hex characters
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        val keep = MAX_LENGTH - (dest.length - (dend - dstart))
        if (keep <= 0 || (dstart == 0 && dend == 0 && dest.startsWith(PREFIX))) {
            // do not exceed max length and do not allow input in front of prefix
            return ""
        } else if (start == end) {
            // allow deletion except just the prefix
            return if (dstart == 0 && dend >= 1 && dest.length > dend - dstart) {
                PREFIX.toString()
            } else {
                null
            }
        }

        val sourceSubSequence = source.subSequence(start, end)
        val shouldAddPrefix = (dstart == 0 && !sourceSubSequence.startsWith(PREFIX))

        var result = sourceSubSequence
            .filterIndexed { index, c ->
                // allow first character to be #
                (dstart == 0 && index == 0 && c == PREFIX) || isHexCharacter(c)
            }.toString()

        if (shouldAddPrefix) {
            result = PREFIX + result
        }

        if (result.length > keep) {
            result = result.substring(0, keep)
        }

        return result.toUpperCase()
    }

    private fun isHexCharacter(ch: Char): Boolean {
        return ch in ('0'..'9') || ch.toUpperCase() in ('A'..'F')
    }
}