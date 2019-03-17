package android.raywenderlich.com.puppyparadise

import android.view.ViewGroup
import android.widget.ImageView

interface BlurryView {

  fun applyBlur(rootView: ViewGroup, blurryBackgroundView: ImageView)

  companion object {

    const val DEFAULT_BLUR_RADIUS = 12
    const val DEFAULT_BLUR_DOWNSCALE = 3
  }
}