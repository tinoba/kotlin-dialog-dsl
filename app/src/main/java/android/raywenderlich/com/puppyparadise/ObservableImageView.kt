package android.raywenderlich.com.puppyparadise

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

class ObservableImageView : AppCompatImageView {

  constructor(context: Context?) : super(context)
  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

  var onImageDrawableLoadedAction: () -> Unit = {}

  override fun setImageDrawable(drawable: Drawable?) {
    super.setImageDrawable(drawable)
    onImageDrawableLoadedAction.invoke()
  }
}