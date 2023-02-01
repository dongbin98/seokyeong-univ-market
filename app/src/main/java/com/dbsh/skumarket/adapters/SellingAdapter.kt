package com.dbsh.skumarket.adapters



import java.text.SimpleDateFormat
import java.util.*
import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dbsh.skumarket.R
import com.dbsh.skumarket.databinding.SellingItemBinding

import com.dbsh.skumarket.model.SellingModelData
import com.dbsh.skumarket.views.LoadSellingInfoActivity



class SellingAdapter(val onItemClicked: (SellingModelData) -> Unit) : ListAdapter<SellingModelData, SellingAdapter.ViewHolder>(
    diffUtil
) {
    inner class ViewHolder(private val binding: SellingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val txtTitle: TextView = itemView.findViewById(R.id.titleText)
        private val txtPrice: TextView = itemView.findViewById(R.id.priceText)
        private val txtDate: TextView = itemView.findViewById(R.id.dateText)
        private val txtImg: ImageView = itemView.findViewById(R.id.thumbnailImg)
        @SuppressLint("SimpleDateFormat", "SuspiciousIndentation")
        fun bind(sellingModelData: SellingModelData) {

            // Long 형식에서 날짜로 바꾸기.
            txtTitle.text = sellingModelData.title
            txtPrice.text = sellingModelData.price
            txtDate.text = sellingModelData.posttime
            Glide.with(itemView).load(sellingModelData.imageUrl).into(txtImg)

            binding.sell = sellingModelData
            // glide로 이미지 불러오기;
            if (sellingModelData.imageUrl.isNotEmpty()) {
                Glide.with(binding.thumbnailImg)
                    .load(sellingModelData.imageUrl)
                    .into(binding.thumbnailImg)
            }

            binding.root.setOnClickListener {
                onItemClicked(sellingModelData)
                val intent = Intent(binding.root.context, LoadSellingInfoActivity::class.java)
                intent.putExtra("data", sellingModelData);
                intent.run { binding.root.context.startActivity(this)}
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SellingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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