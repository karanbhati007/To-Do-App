package com.ksb.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ksb.roomdemo.databinding.ActivityMainBinding
import com.ksb.roomdemo.db.Subscriber
import com.ksb.roomdemo.db.SubscriberDatabase

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity2"
    private lateinit var viewModel: SubsViewModel
    private lateinit var databinding: ActivityMainBinding
    private lateinit var mAdapter : MyRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = SubscriberDatabase.getInstance(application).subsDao
        val repository = SubscriberRepository(dao)

        viewModel = ViewModelProvider(
            this,
            SubscriberViewModelFactory(repository)
        ).get(SubsViewModel::class.java)

        databinding.mViewModel = viewModel
        databinding.lifecycleOwner = this // as we are using LiveData with DataBinding


        // Setting up recycler View
     //   databinding.recyclerView.adapter =

        initRecyclerView()

        viewModel.sendMessage.observe(this,{
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
            }
        })

    }

    private fun initRecyclerView() {
        databinding.recyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter =  MyRecyclerViewAdapter{ viewModel.initUpdateOrDelete(it) }
        databinding.recyclerView.adapter = mAdapter
        displaySubsList()
    }



    private fun displaySubsList() {



        viewModel.subscribers.observe(this, { list ->
            mAdapter.setList(list)
            mAdapter.notifyDataSetChanged()

/*            list.forEach {
                Log.i(TAG, it.toString())
            }*/
        })
    }
}