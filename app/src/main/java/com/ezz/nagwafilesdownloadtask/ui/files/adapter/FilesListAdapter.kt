package com.ezz.nagwafilesdownloadtask.ui.files.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ezz.nagwafilesdownloadtask.data.models.FileItem
import com.ezz.nagwafilesdownloadtask.databinding.ItemFileBinding


class FilesListAdapter(
    private val mContext: Context,
    val downloadClickListener: DownloadClickListener
) : RecyclerView.Adapter<FilesViewHolder>() {
    val dataList: ArrayList<FileItem> = arrayListOf()
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): FilesViewHolder {
        val binding = ItemFileBinding.inflate(LayoutInflater.from(mContext), viewGroup, false)

        return FilesViewHolder(
            binding as ItemFileBinding,
            mContext,
            downloadClickListener
        )
    }

    override fun onBindViewHolder(
        viewHolder: FilesViewHolder,
        position: Int
    ) {
        viewHolder.bind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size


    fun addItems(data: ArrayList<FileItem>) {
        val positionStart = dataList.size
        dataList.addAll(data)
        notifyItemRangeInserted(positionStart, data.size)
    }

    fun addItem(item: FileItem, position: Int = dataList.size) {
        dataList.add(item)
        notifyItemInserted(position)
    }

    fun replaceItems(newItems: ArrayList<FileItem>) {
        dataList.clear()
        dataList.addAll(newItems)
        notifyDataSetChanged()
    }

    fun updateItem(index: Int, item: FileItem) {
        dataList[index] = item
        notifyItemChanged(index)
    }

    fun deleteItem(index: Int) {
        if ((dataList.size - 1) >= index) {
            dataList.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun clearItems() {
        dataList.clear()
        notifyDataSetChanged()
    }
}