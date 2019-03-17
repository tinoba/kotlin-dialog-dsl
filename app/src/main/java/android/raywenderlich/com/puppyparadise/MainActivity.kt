package android.raywenderlich.com.puppyparadise

import android.os.Bundle
import android.raywenderlich.com.puppyparadise.DialogPopupView.Companion.ALPHA_TRANSPARENT
import android.raywenderlich.com.puppyparadise.DialogPopupView.Companion.FADING_ANIMATION_DURATION
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), PuppyAdapter.ItemClickListener {

  private val adapter by lazy { PuppyAdapter() }

  private var dialogPopupView: DialogPopupView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    initUi()
  }

  private fun initUi() {
    puppyRecyclerView.layoutManager = LinearLayoutManager(this)
    puppyRecyclerView.adapter = adapter
    val a = listOf(R.drawable.samoyed,
        R.drawable.shiba,
        R.drawable.siberian_husky,
        R.drawable.akita,
        R.drawable.german_shepherd,
        R.drawable.golden_retriever)
    adapter.setListener(this)
    adapter.setData(a)
  }

  private fun createMessagePopupView(titleText: String,
                                     negativeText: String,
                                     positiveText: String,
                                     positiveClickAction: () -> Unit,
                                     cancelActionClick: () -> Unit) =
      DialogPopupView.builder(this)
          .viewToBlur(rootView)
          .titleText(titleText)
          .negativeText(negativeText)
          .positiveText(positiveText)
          .onCancelClickAction(cancelActionClick)
          .onPositiveClickAction(positiveClickAction)
          .build()

  private fun showGeneralMessageDialog() {
    createMessagePopupView("title",
        "negative",
        "positive", {}, { removeMessagePopup() }).let {
      dialogPopupView = it
      addMessagePopupViewWithBlurToRoot(it)
    }
  }

  private fun addMessagePopupViewWithBlurToRoot(dialogPopupView: DialogPopupView) {
    rootView.addView(dialogPopupView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    dialogPopupView.applyBlur()
  }

  private fun removeMessagePopup() {
    dialogPopupView?.let {
      it.animate()
          .alpha(ALPHA_TRANSPARENT)
          .setDuration(FADING_ANIMATION_DURATION)
          .withEndAction {
            rootView.removeView(it)
          }
          .start()
      dialogPopupView = null
    }
  }

  override fun onItemClicked(position: Int) {
    showGeneralMessageDialog()
  }
}
