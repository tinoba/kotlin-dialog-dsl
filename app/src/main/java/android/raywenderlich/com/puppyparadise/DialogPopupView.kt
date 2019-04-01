/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package android.raywenderlich.com.puppyparadise

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.view_dialog_popup.view.*

class DialogPopupView(context: Context?,
                      private val viewToBlur: View?,
                      private val titleText: String,
                      private val negativeText: String,
                      private val positiveText: String,
                      private val onBackgroundClickAction: () -> Unit,
                      private val onNegativeClickAction: () -> Unit,
                      private val onPositiveClickAction: () -> Unit
) : FrameLayout(context!!) {

  companion object {

    const val FADING_ANIMATION_DURATION = 200L

    const val ALPHA_TRANSPARENT = 0.0f
    const val ALPHA_OPAQUE = 1.0f

    private const val DEFAULT_BLUR_RADIUS = 12
    private const val DEFAULT_BLUR_DOWNSCALE = 3

    @LayoutRes
    private const val LAYOUT_RESOURCE = R.layout.view_dialog_popup
  }

  private var blurRadius = DEFAULT_BLUR_RADIUS
  private var blurDownscaleRadius = DEFAULT_BLUR_DOWNSCALE

  init {
    alpha = ALPHA_TRANSPARENT

    inflateLayout(context!!)
    fillContent()
    setClickListeners()
  }

  private fun inflateLayout(context: Context) = LayoutInflater.from(context).inflate(LAYOUT_RESOURCE, this, true)

  private fun fillContent() {
    blurryBackground.onImageDrawableLoadedAction = this::fadeInView
    title.text = titleText
    negativeTextView.text = negativeText
    positiveTextView.text = positiveText
  }

  private fun setClickListeners() {
    blurryBackground.setOnClickListener { onBackgroundClickAction.invoke() }
    negativeTextView.setOnClickListener { onNegativeClickAction.invoke() }
    positiveTextView.setOnClickListener { onPositiveClickAction.invoke() }
  }

  private fun fadeInView() {
    cardView.visibility = View.VISIBLE
    animate().alpha(ALPHA_OPAQUE)
        .setDuration(FADING_ANIMATION_DURATION)
        .start()
  }

  fun applyBlur() {
    viewToBlur.let {
      Blurry.with(context)
          .async()
          .radius(blurRadius)
          .sampling(blurDownscaleRadius)
          .capture(it)
          .into(blurryBackground)
    }
  }

  @PuppyDslMarker class DialogPopupBuilder {
    var context: Context? = null
    var viewToBlur: View? = null
    var titleText: String = ""
    var negativeText: String = ""
    var positiveText: String = ""
    var onBackgroundClickAction: () -> Unit = {}
    var onNegativeClickAction: () -> Unit = {}
    var onPositiveClickAction: () -> Unit = {}

    inline fun with(context: () -> Context) {
      this.context = context()
    }

    inline fun viewToBlur(viewToBlur: () -> View) {
      this.viewToBlur = viewToBlur()
    }

    inline fun titleText(title: () -> String) {
      this.titleText = title()
    }

    inline fun negativeText(negativeText: () -> String) {
      this.negativeText = negativeText()
    }

    inline fun positiveText(positiveText: () -> String) {
      this.positiveText = positiveText()
    }

    inline fun onNegativeClickAction(onNegativeClickAction: () -> () -> Unit) {
      this.onNegativeClickAction = onNegativeClickAction()
    }

    inline fun onPositiveClickAction(onPositiveClickAction: () -> () -> Unit) {
      this.onPositiveClickAction = onPositiveClickAction()
    }

    inline fun onBackgroundClickAction(onBackgroundClickAction: () -> () -> Unit) {
      this.onBackgroundClickAction = onBackgroundClickAction()
    }

    fun build() = DialogPopupView(context,
        viewToBlur,
        titleText,
        negativeText,
        positiveText,
        onBackgroundClickAction,
        onNegativeClickAction,
        onPositiveClickAction)
  }
}