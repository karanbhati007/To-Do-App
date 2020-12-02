package com.ksb.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ksb.roomdemo.databinding.ListItemBinding
import com.ksb.roomdemo.db.Subscriber

class MyRecyclerViewAdapter(private val sendInfoListener : (Subscriber)->Unit) : RecyclerView.Adapter<MyRecyclerViewAdapter.MyVIewHolder>() {

    private var subsList = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyVIewHolder {
      // val v : View = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)

        val inflater = LayoutInflater.from(parent.context)
        val binding : ListItemBinding = DataBindingUtil.inflate(inflater,R.layout.list_item,parent,false)
        return MyVIewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyVIewHolder, position: Int) {
        holder.bindData(subsList[position],sendInfoListener)

    }

    override fun getItemCount(): Int  = subsList.size

    fun setList(subs : List<Subscriber>){
        subsList.clear()
        subsList.addAll(subs)
    }

    class MyVIewHolder(private val binding : ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(sub: Subscriber, sendInfoListener: (Subscriber) -> Unit){

            binding.name.text = sub.name
            binding.email.text = sub.email
           // itemView.findViewById<TextView>(R.id.name).text = sub.name
           // itemView.findViewById<TextView>(R.id.email).text = sub.email
           // itemView.setOnClickListener {

           // }
            binding.root.setOnClickListener {
                sendInfoListener(sub)
            }
        }

    }
}