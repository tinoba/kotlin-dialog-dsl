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

import android.os.Bundle
import android.raywenderlich.com.puppyparadise.DialogPopupView.Companion.ALPHA_TRANSPARENT
import android.raywenderlich.com.puppyparadise.DialogPopupView.Companion.FADING_ANIMATION_DURATION
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_puppy.*

class PuppyActivity : AppCompatActivity(), PuppyAdapter.ItemClickListener {

  private var puppies: List<Puppy> = puppyViewModel {
    puppies {
      puppy {
        isLiked = false
        imageResourceId = R.drawable.samoyed
      }
      puppy {
        isLiked = false
        imageResourceId = R.drawable.shiba
      }
      puppy {
        isLiked = false
        imageResourceId = R.drawable.siberian_husky
      }
      puppy {
        isLiked = false
        imageResourceId = R.drawable.akita
      }
      puppy {
        isLiked = false
        imageResourceId = R.drawable.german_shepherd
      }
      puppy {
        isLiked = false
        imageResourceId = R.drawable.golden_retriever
      }
    }
  }

  private val adapter by lazy { PuppyAdapter() }

  private var dialogPopupView: DialogPopupView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_puppy)

    initUi()
  }

  private fun initUi() {
    puppyRecyclerView.layoutManager = LinearLayoutManager(this)
    puppyRecyclerView.adapter = adapter

    adapter.setListener(this)
    adapter.setData(puppies)
  }

  private fun createDialogPopup(titleText: String,
                                negativeText: String,
                                positiveText: String,
                                backgroundClickAction: () -> Unit,
                                positiveClickAction: () -> Unit,
                                negativeClickAction: () -> Unit) =
      buildDialog(this) {
        viewToBlur(rootView)
        title(titleText)
        positiveAction(positiveText) { positiveClickAction }
        negativeAction(negativeText) { negativeClickAction }
        backgroundAction { backgroundClickAction }
      }

  private fun showDialogPopup(position: Int) {
    createDialogPopup(getString(R.string.dialog_title),
        getString(R.string.dialog_negative_answer),
        getString(R.string.dialog_positive_answer),
        { removeDialogPopup() },
        {
          removeDialogPopup()
          changeLikeStatus(true, position)
          adapter.setData(puppies)
        },
        {
          removeDialogPopup()
          changeLikeStatus(false, position)
          adapter.setData(puppies)
        }).let {
      dialogPopupView = it
      addDialogPopupViewWithBlurToRoot(it)
    }
  }

  private fun changeLikeStatus(isLiked: Boolean, position: Int) {
    puppies = puppies.mapIndexed { index, puppy ->
      if (index == position) {
        Puppy(isLiked, puppy.imageResource)
      } else {
        puppy
      }
    }
  }

  private fun addDialogPopupViewWithBlurToRoot(dialogPopupView: DialogPopupView) {
    rootView.addView(dialogPopupView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    dialogPopupView.applyBlur()
  }

  private fun removeDialogPopup() {
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

  override fun onItemClicked(position: Int) = showDialogPopup(position)
}
