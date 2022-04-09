package ru.tech.averagemark

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import ru.tech.averagemark.ui.theme.*
import kotlin.math.roundToInt

class MainViewModel : ViewModel() {

    var needUpdate by mutableStateOf(true)

    private val _color = mutableStateOf(color_0)
    val color: State<Color> = _color

    private val _text = mutableStateOf("")
    val text: State<String> = _text

    private val _averageState = mutableStateOf("")
    val averageState: State<String> = _averageState

    fun updateText(newText: String) {
        _text.value += newText

        _text.value = _text.value.filter { it != ' ' }.map { it.toString() }.beatify()
        _averageState.value =
            _text.value.filter { it != ' ' }.map { it.toString().toInt() }.let {
                updateColor(it)
            }

        needUpdate = false
    }

    private fun updateColor(it: List<Int>): String {
        return if (it.isNotEmpty()) {
            val ratio = (it.sum() / it.size.toDouble() * 100.0).roundToInt() / 100.0
            _color.value = when {
                ratio < 2.5 -> color_2
                ratio < 3.5 -> color_3
                ratio < 4.6 -> color_4
                ratio <= 5 -> color_5
                else -> color_0
            }
            ratio.toString()
        } else ""
    }

    fun removeLast() {
        _text.value = _text.value.filter { it != ' ' }.dropLast(1).map { it.toString() }.beatify()

        _averageState.value =
            _text.value.filter { it != ' ' }.map { it.toString().toInt() }.let {
                updateColor(it)
            }

        if (_text.value.isEmpty()) _color.value = color_0
    }

    fun save(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putString("AVERAGE", text.value.filter { it != ' ' }).apply()
    }

    fun removeAll() {
        _text.value = ""
        _averageState.value = ""
        _color.value = color_0
    }

}

private fun List<String>.beatify(): String {
    var `return` = ""
    var cnt = 0
    forEach {
        if (cnt == 4) {
            `return` += " $it"
            cnt = 0
        } else {
            `return` += it
        }

        cnt++
    }
    return `return`
}
