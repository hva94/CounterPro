package com.hvasoft.counterpro.presentation.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemCounterBinding.bind(view)
        fun setListener(counter: Counter) {
            with(binding) {
                btnIncrementCounter.setOnClickListener {
                    listener.onIncrementClick(counter)
                }
                btnDecrementCounter.setOnClickListener {
                    listener.onDecrementClick(counter)
                }
                root.setOnLongClickListener {
                    listener.onLongClick(counter)
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
}
