package android.raywenderlich.com.puppyparadise

import android.content.Context
import android.view.View

inline fun buildDialog(context: Context, buildDialog: DialogPopupView.DialogPopupBuilder.() -> Unit): DialogPopupView {
  val builder = DialogPopupView.DialogPopupBuilder()

  builder.context = context
  builder.buildDialog()
  return builder.build()
}

fun DialogPopupView.DialogPopupBuilder.title(title: String) {
  titleText = title
}

fun DialogPopupView.DialogPopupBuilder.viewToBlur(viewToBlur: View) {
  this.viewToBlur = viewToBlur
}

fun DialogPopupView.DialogPopupBuilder.negativeAction(negativeText: String, onNegativeClickAction: () -> () -> Unit) {
  this.onNegativeClickAction = onNegativeClickAction()
  this.negativeText = negativeText
}

fun DialogPopupView.DialogPopupBuilder.positiveAction(positiveText: String, onPositiveClickAction: () -> () -> Unit) {
  this.onPositiveClickAction = onPositiveClickAction()
  this.positiveText = positiveText
}

fun DialogPopupView.DialogPopupBuilder.backgroundAction(onBackgroundClickAction: () -> () -> Unit) {
  this.onBackgroundClickAction = onBackgroundClickAction()
}