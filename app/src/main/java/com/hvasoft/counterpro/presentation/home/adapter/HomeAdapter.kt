package com.hvasoft.counterpro.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hvasoft.counterpro.R
import com.hvasoft.counterpro.databinding.ItemCounterBinding
import com.hvasoft.counterpro.domain.model.Counter

class HomeAdapter(private val listener: OnClickListener) :
    ListAdapter<Counter, RecyclerView.ViewHolder>(CounterDiffCallback()) {

    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_counter, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val counter = getItem(position)
        with(holder as ViewHolder) {
            setListener(counter)
            binding.tvCounterTitle.text = counter.title
            binding.tvCounterValue.text = counter.count.toString()
            binding.viewSelectedBackground.visibility =
                if (counter.isSelected) View.VISIBLE else View.GONE
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCounterBinding.bind(view)
        fun setListener(counter: Counter) {
            with(binding) {
                btnIncrementCounter.setOnClickListener {
                    disableActionMode()
                    listener.onIncrementClick(counter)
                }
                btnDecrementCounter.setOnClickListener {
                    disableActionMode()
                    listener.onDecrementClick(counter)
                }
                root.setOnClickListener {
                    if (isActionModeEnabled.value == true) {
                        currentList[layoutPosition].isSelected =
                            !currentList[layoutPosition].isSelected
                        if (currentList.none { it.isSelected }) {
                            _isActionModeEnabled.value = false
                        }
                        notifyItemChanged(layoutPosition)
                    }
                }
                root.setOnLongClickListener {
                    if (isActionModeEnabled.value == false) {
                        _isActionModeEnabled.value = true
                        currentList[layoutPosition].isSelected =
                            !currentList[layoutPosition].isSelected
                        notifyItemChanged(layoutPosition)
                    }
                    true
                }
            }
        }
    }

    class CounterDiffCallback : DiffUtil.ItemCallback<Counter>() {
        override fun areItemsTheSame(oldItem: Counter, newItem: Counter): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Counter, newItem: Counter): Boolean {
            return oldItem == newItem
        }
    }

    private val _isActionModeEnabled = MutableLiveData(false)
    val isActionModeEnabled: LiveData<Boolean> = _isActionModeEnabled

    fun disableActionMode() {
        _isActionModeEnabled.value = false
        currentList.onEach { it.isSelected = false }
        notifyItemRangeChanged(0, currentList.size)
    }

}
