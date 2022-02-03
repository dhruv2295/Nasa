package com.nasa.image.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nasa.image.database.Nasa
import com.nasa.image.databinding.NasaItemBinding

class NasaListAdapter(private var list: List<Nasa>): RecyclerView.Adapter<NasaListAdapter.ViewHolder>() {

    private lateinit var context: Context
    class ViewHolder(val binding: NasaItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(NasaItemBinding.inflate(
            LayoutInflater.from( parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text = list[position].title
        holder.binding.description.text = list[position].explanation
        Glide.with(context).load(list[position].url).into(holder.binding.image)

    }

    fun updateList(list: List<Nasa>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount() = list.size
}