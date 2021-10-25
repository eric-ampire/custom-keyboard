package com.lottiefiles.app.android.demokeyboard

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.media.AudioManager
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View

@Suppress("DEPRECATION")
class LottiefilesKeyboard : InputMethodService(), KeyboardView.OnKeyboardActionListener {

  private lateinit var keyboard: Keyboard
  private lateinit var keyboardView: KeyboardView

  private var isCaps = false

  override fun onCreateInputView(): View {
    val layoutInflater = LayoutInflater.from(this)
    keyboardView = layoutInflater.inflate(R.layout.keyboard_layout, null, false) as KeyboardView
    keyboard = Keyboard(this, R.xml.custom_keyboard)
    return keyboardView.apply {
      keyboard = this@LottiefilesKeyboard.keyboard
      setOnKeyboardActionListener(this@LottiefilesKeyboard)
    }
  }

  override fun onPress(p0: Int) {
    
  }

  override fun onRelease(p0: Int) {

  }

  override fun onKey(key: Int, p1: IntArray?) {
    val inputConnection = currentInputConnection
    when (key) {
      Keyboard.KEYCODE_DELETE -> inputConnection.deleteSurroundingText(1, 0)
      Keyboard.KEYCODE_SHIFT -> {
        isCaps = !isCaps
        keyboard.isShifted = isCaps
        keyboardView.invalidateAllKeys()
      }
      Keyboard.KEYCODE_DONE -> {
        inputConnection.sendKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER))
      }
      else -> {
        var code = key.toChar()
        if (code.isLetter() && isCaps) {
          code = code.uppercaseChar()
        }

        inputConnection.commitText("$code", 1)
      }
    }
    playClick(key)
  }

  private fun playClick(i: Int) {
    val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
    when (i) {
      32 -> audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR)
      Keyboard.KEYCODE_DONE, 10 ->  audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN)
      Keyboard.KEYCODE_DELETE ->  audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE)
      else ->  audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
    }
  }

  override fun onText(p0: CharSequence?) {
    
  }

  override fun swipeLeft() {
    
  }

  override fun swipeRight() {
    
  }

  override fun swipeDown() {
    
  }

  override fun swipeUp() {
    
  }
}