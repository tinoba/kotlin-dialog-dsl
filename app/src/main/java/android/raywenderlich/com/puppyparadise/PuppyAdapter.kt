package android.raywenderlich.com.puppyparadise

import android.os.SystemClock
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.puppy_item.view.*

class PuppyAdapter : RecyclerView.Adapter<PuppyAdapter.PuppyViewHolder>() {

  companion object {
    private const val THROTTLE_DURATION_MILLIS = 500
  }

  interface ItemClickListener {
    fun onItemClicked(position: Int)
  }

  private val items = mutableListOf<Int>()

  private var lastClickedTime = 0L

  private lateinit var listener: ItemClickListener

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PuppyViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.puppy_item, parent, false)
    return PuppyViewHolder(view)
  }

  fun setListener(listener: ItemClickListener) {
    this.listener = listener
  }

  fun setData(newItems: List<Int>) {
    items.clear()
    items.addAll(newItems)
    notifyDataSetChanged()
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: PuppyViewHolder, position: Int) = holder.showData(items[position], position)

  inner class PuppyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun showData(imageResourceId: Int, position: Int) = with(itemView) {
      Picasso.get()
          .load(imageResourceId)
          .into(image)

      itemView.setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickedTime >= THROTTLE_DURATION_MILLIS) {
          lastClickedTime = SystemClock.elapsedRealtime()
          listener.onItemClicked(position)
        }
      }
    }
  }
}