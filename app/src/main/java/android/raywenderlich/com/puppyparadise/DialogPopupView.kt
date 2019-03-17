package android.raywenderlich.com.puppyparadise

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.view_dialog_popup.view.*

class DialogPopupView private constructor(context: Context, builder: Builder) : FrameLayout(context) {

  companion object {

    const val FADING_ANIMATION_DURATION = 200L

    const val ALPHA_TRANSPARENT = 0.0f
    const val ALPHA_OPAQUE = 1.0f

    private const val DEFAULT_BLUR_RADIUS = 12
    private const val DEFAULT_BLUR_DOWNSCALE = 3

    @LayoutRes
    private const val LAYOUT_RESOURCE = R.layout.view_dialog_popup

    fun builder(context: Context): Builder = Builder(context)
  }

  private var viewToBlur: View? = null
  private var blurRadius = DEFAULT_BLUR_RADIUS
  private var blurDownscaleRadius = DEFAULT_BLUR_DOWNSCALE
  private var titleText: String = ""
  private var cancelText: String = ""
  private var deleteText: String = ""

  private var onBackgroundClickAction: () -> Unit = {}
  private var onNegativeClickAction: () -> Unit = {}
  private var onPositiveClickAction: () -> Unit = {}

  init {
    viewToBlur = builder.viewToBlur
    titleText = builder.titleText
    cancelText = builder.cancelText
    deleteText = builder.deleteText
    onBackgroundClickAction = builder.onBackgroundClickAction
    onNegativeClickAction = builder.onCancelClickAction
    onPositiveClickAction = builder.onDeleteClickAction

    alpha = ALPHA_TRANSPARENT

    inflateLayout(context)
    fillContent()
    setClickListeners()
  }

  private fun inflateLayout(context: Context) = LayoutInflater.from(context).inflate(LAYOUT_RESOURCE, this, true)

  private fun fillContent() {
    blurryBackground.onImageDrawableLoadedAction = this::fadeInView
    title.text = titleText
    negativeTextView.text = cancelText
    positiveTextView.text = deleteText
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
    viewToBlur?.let {
      Blurry.with(context)
          .async()
          .radius(blurRadius)
          .sampling(blurDownscaleRadius)
          .capture(it)
          .into(blurryBackground)
    }
  }

  class Builder(val context: Context) {

    var viewToBlur: View? = null
      private set

    var titleText: String = ""
      private set

    var cancelText: String = ""
      private set

    var deleteText: String = ""
      private set

    var onBackgroundClickAction: () -> Unit = {}
      private set

    var onCancelClickAction: () -> Unit = {}
      private set

    var onDeleteClickAction: () -> Unit = {}
      private set

    fun viewToBlur(viewToBlur: View) = apply { this.viewToBlur = viewToBlur }

    fun titleText(titleText: String) = apply { this.titleText = titleText }

    fun negativeText(cancelText: String) = apply { this.cancelText = cancelText }

    fun positiveText(deleteText: String) = apply { this.deleteText = deleteText }

    fun onCancelClickAction(onCancelClickAction: () -> Unit) = apply { this.onCancelClickAction = onCancelClickAction }

    fun onPositiveClickAction(onDeleteClickAction: () -> Unit) = apply { this.onDeleteClickAction = onDeleteClickAction }

    fun onBackgroundClickAction(onBackgroundClickAction: () -> Unit) = apply { this.onBackgroundClickAction = onBackgroundClickAction }

    fun build(): DialogPopupView = DialogPopupView(context, this)
  }
}