package com.esracangungor.todolistapp.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.esracangungor.todolistapp.databinding.ItemWidgetCategoryBinding

class WidgetCategorySelectionAdapter(
    private val categories: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<WidgetCategorySelectionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWidgetCategoryBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(
            categories[position], onClick
        )

    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class ViewHolder(private val binding: ItemWidgetCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            accountId: String,
            onClick: (String) -> Unit
        ) {

            binding.tvWidgetCategoryName.text = accountId
            binding.root.setOnClickListener {
                onClick(accountId)
            }
        }
    }
}