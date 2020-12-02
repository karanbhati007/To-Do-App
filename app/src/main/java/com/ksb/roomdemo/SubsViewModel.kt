package com.ksb.roomdemo


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ksb.roomdemo.db.Subscriber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SubsViewModel(private val repository: SubscriberRepository) : ViewModel() {

    private var isUpdateOrDelete: Boolean = false
    private lateinit var mSub: Subscriber
    private var _sendMessage = MutableLiveData<Event<String>>()

    val sendMessage:LiveData<Event<String>>
    get() = _sendMessage

    val subscribers = repository.getAllSubs

    val inputName = MutableLiveData<String>()
    val inputEmail = MutableLiveData<String>()


    val saveOrUpdate = MutableLiveData<String>()
    val clearAllOrDelete = MutableLiveData<String>()

    init {
        saveOrUpdate.value = "Save"
        clearAllOrDelete.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (isUpdateOrDelete) {
            updateSubs(mSub)
        } else {
            if (inputName.value != null && inputEmail.value != null) {
                insertSubs(
                    Subscriber(
                        0,
                        name = inputName.value!!,
                        email = inputEmail.value!!
                    )
                )  // id will automatically generated by ROOM
                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            deleteSubs(mSub)
        } else {
            deleteAll()
        }
    }

    private fun insertSubs(subs: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
        val newRowId = repository.insertSubs(subs)
        withContext(Dispatchers.Main) {
            if (newRowId > -1) {
                _sendMessage.value = Event("Note Inserted Successfully, Row no. : $newRowId")

            } else {
                _sendMessage.value = Event("Error Occurred !!")
            }
        }
    }

    private fun updateSubs(subs: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateSubs(Subscriber(subs.id,inputName.value!!, inputEmail.value!!))
        withContext(Dispatchers.Main) {
            backToSaveClear()
            _sendMessage.value = Event("Note Updated Successfully")
        }
    }

    private fun deleteSubs(subs: Subscriber) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteSubs(subs)
        withContext(Dispatchers.Main) {
            backToSaveClear()
            _sendMessage.value = Event("Note Deleted Successfully")
        }
    }

    private fun backToSaveClear() {
        saveOrUpdate.value = "Save"
        clearAllOrDelete.value = "Clear All"
        inputName.value = null
        inputEmail.value = null
        isUpdateOrDelete = false
    }

    private fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
        withContext(Dispatchers.Main){
            _sendMessage.value = Event("All Notes Deleted Successfully")
        }
    }


    fun initUpdateOrDelete(sub: Subscriber) {
        isUpdateOrDelete = true
        mSub = sub
        saveOrUpdate.value = "Update"
        clearAllOrDelete.value = "Delete"
        inputName.value = sub.name
        inputEmail.value = sub.email
    }


/*    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }*/

}