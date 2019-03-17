package android.raywenderlich.com.puppyparadise

import android.content.Context
import android.raywenderlich.com.puppyparadise.BlurryViewImpl.Companion.ALPHA_TRANSPARENT
import android.raywenderlich.com.puppyparadise.BlurryViewImpl.Companion.FADING_ANIMATION_DURATION
import android.raywenderlich.com.puppyparadise.DialogPopupView.Companion.ALPHA_OPAQUE
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_dialog_popup.view.*

class DialogPopup : CardView {

  private var positiveAction: () -> Unit = {}
  private var negativeAction: () -> Unit = {}

  constructor(context: Context, title: String, message: String, negativeButtonText: String, positiveButtonText: String) : super(context) {
    init(title, message, negativeButtonText, positiveButtonText)
  }

  constructor(context: Context, title: String, message: String, negativeButtonText: String, positiveButtonText: String,
              attrs: AttributeSet?) : super(context, attrs) {
    init(title, message, negativeButtonText, positiveButtonText)
  }

  constructor(context: Context, title: String, message: String, negativeButtonText: String, positiveButtonText: String,
              attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
    init(title, message, negativeButtonText, positiveButtonText)
  }

  private fun init(title: String, message: String, negativeButtonText: String, positiveButtonText: String) {
    View.inflate(context, R.layout.view_dialog_popup, this)

    alpha = ALPHA_TRANSPARENT
    blurryBackground.onImageDrawableLoadedAction = ::fadeInView
    val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
    setLayoutParams(layoutParams)

    negativeTextView.visibility = if (negativeButtonText.isEmpty()) View.GONE else View.VISIBLE
    positiveTextView.text = title
    //messageTextView?.text = message
    negativeTextView.text = negativeButtonText
    positiveTextView.text = positiveButtonText
    setClickListeners()
  }

  private fun fadeInView() {
    animate().alpha(ALPHA_OPAQUE)
        .setDuration(FADING_ANIMATION_DURATION)
        .start()
  }

  fun setButtonActions(positiveAction: () -> Unit, negativeAction: () -> Unit) {
    this.positiveAction = positiveAction
    this.negativeAction = negativeAction
  }

  fun setClickListeners() {
    negativeTextView.setOnClickListener { negativeAction.invoke() }
    positiveTextView.setOnClickListener {
      positiveAction.invoke()
      negativeAction.invoke()
    }
  }
}