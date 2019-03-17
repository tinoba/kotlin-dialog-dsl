package android.raywenderlich.com.puppyparadise

import android.content.Context
import android.raywenderlich.com.puppyparadise.BlurryView.Companion.DEFAULT_BLUR_DOWNSCALE
import android.raywenderlich.com.puppyparadise.BlurryView.Companion.DEFAULT_BLUR_RADIUS
import android.view.ViewGroup
import android.widget.ImageView
import jp.wasabeef.blurry.Blurry

class BlurryViewImpl(private val context: Context) : BlurryView {

  override fun applyBlur(rootView: ViewGroup, blurryBackgroundView: ImageView) {

    Blurry.with(context)
        .async()
        .radius(DEFAULT_BLUR_RADIUS)
        .sampling(DEFAULT_BLUR_DOWNSCALE)
        .capture(rootView)
        .into(blurryBackgroundView)
  }

  companion object {

    var FADING_ANIMATION_DURATION = 200L
    var ALPHA_TRANSPARENT = 0.0f
    var ALPHA_OPAQUE = 1.0f
  }
}