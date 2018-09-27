package eo.view.colorinput

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.view_color_input.view.*


class ColorInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), SeekBar.OnSeekBarChangeListener, TextWatcher,
    View.OnFocusChangeListener {

    companion object {
        private const val DEFAULT_COLOR = Color.BLACK
    }

    interface ColorChangeListener {
        fun onColorChanged(color: Int)
    }

    var color: Int
        get() = Color.argb(alpha, red, green, blue)
        set(value) {
            if (color != value) {
                alpha = Color.alpha(value)
                red = Color.red(value)
                green = Color.green(value)
                blue = Color.blue(value)

                updateHexInput()
                colorChangeListener?.onColorChanged(value)
            }
        }

    private var alpha
        get() = alphaSeekBar.progress
        set(value) {
            alphaSeekBar.progress = value
            alphaValueView.text = value.toString()
        }

    private var red
        get() = redSeekBar.progress
        set(value) {
            redSeekBar.progress = value
            redValueView.text = value.toString()
        }

    private var green
        get() = greenSeekBar.progress
        set(value) {
            greenSeekBar.progress = value
            greenValueView.text = value.toString()
        }

    private var blue
        get() = blueSeekBar.progress
        set(value) {
            blueSeekBar.progress = value
            blueValueView.text = value.toString()
        }

    var colorChangeListener: ColorChangeListener? = null

    init {
        inflate(context, R.layout.view_color_input, this)

        hexEditText.filters = arrayOf(HexColorInputFilter())
        setupEventHandlers()
        color = DEFAULT_COLOR
        updateHexInput()
    }

    private fun setupEventHandlers() {
        hexEditText.addTextChangedListener(this)
        hexEditText.onFocusChangeListener = this

        alphaSeekBar.setOnSeekBarChangeListener(this)
        redSeekBar.setOnSeekBarChangeListener(this)
        greenSeekBar.setOnSeekBarChangeListener(this)
        blueSeekBar.setOnSeekBarChangeListener(this)
    }

    private fun updateHexInput() {
        hexEditText.removeTextChangedListener(this)
        hexEditText.setText(context.getString(R.string.color_input_hex_value, color))
        hexEditText.addTextChangedListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (!fromUser) return

        when (seekBar) {
            alphaSeekBar -> alpha = progress
            redSeekBar -> red = progress
            greenSeekBar -> green = progress
            blueSeekBar -> blue = progress
        }

        colorChangeListener?.onColorChanged(color)
        hexEditText.clearFocus()
        updateHexInput()
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        val colorInt = if (s.isEmpty()) {
            0
        } else {
            val colorHex = s.padEnd(HexColorInputFilter.MAX_LENGTH, '0').toString()
            Color.parseColor(colorHex)
        }

        alpha = Color.alpha(colorInt)
        red = Color.red(colorInt)
        green = Color.green(colorInt)
        blue = Color.blue(colorInt)
        colorChangeListener?.onColorChanged(color)
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (view == hexEditText && !hasFocus) {
            updateHexInput()

            // hide keyboard
            ContextCompat.getSystemService(context, InputMethodManager::class.java)
                ?.hideSoftInputFromWindow(hexEditText.windowToken, 0)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        // no-op
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        // no-op
    }

    override fun afterTextChanged(s: Editable?) {
        // no-op
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        // no-op
    }
}