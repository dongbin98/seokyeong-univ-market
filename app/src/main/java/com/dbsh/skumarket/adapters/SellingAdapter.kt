package com.dbsh.skumarket.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbsh.skumarket.databinding.SellingItemBinding
import com.dbsh.skumarket.model.SellingModelData
import java.text.SimpleDateFormat
import java.util.*

class SellingAdapter(val onItemClicked: (SellingModelData) -> Unit) : ListAdapter<SellingModelData, SellingAdapter.ViewHolder>(
    diffUtil,
) {
    inner class ViewHolder(private val binding: SellingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SimpleDateFormat")
        fun bind(sellingModelData: SellingModelData) {
            // Long 형식에서 날짜로 바꾸기.
            val format = SimpleDateFormat("MM월 dd일")
            val date = Date(sellingModelData.posttime) // Long -> Date

            binding.titleText.text = sellingModelData.title
            binding.dateText.text = format.format(date).toString()
            binding.priceText.text = sellingModelData.price

            // glide로 이미지 불러오기;
            if (sellingModelData.imageUrl.isNotEmpty()) {
                Glide.with(binding.thumbnailImg)
                    .load(sellingModelData.imageUrl)
                    .into(binding.thumbnailImg)
            }

            binding.root.setOnClickListener {
                onItemClicked(sellingModelData)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SellingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<SellingModelData>() {
            override fun areItemsTheSame(oldItem: SellingModelData, newItem: SellingModelData): Boolean {
                // 현재 노출하고 있는 아이템과 새로운 아이템이 같은지 비교
                return oldItem.posttime == newItem.posttime
            }

            override fun areContentsTheSame(oldItem: SellingModelData, newItem: SellingModelData): Boolean {
                // equals 비교
                return oldItem == newItem
            }
        }
    }
}
