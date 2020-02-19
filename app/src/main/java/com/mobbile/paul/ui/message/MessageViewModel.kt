package com.mobbile.paul.ui.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobbile.paul.model.ChatMessage
import com.mobbile.paul.provider.Repository
import javax.inject.Inject

class MessageViewModel @Inject constructor(private val repo: Repository): ViewModel() {

    fun getMessage() : LiveData<List<ChatMessage>> {
        val result = MutableLiveData<List<ChatMessage>>()
        repo.fetchMessages()
            .subscribe({
                result.postValue(it)
            },{
                result.postValue(emptyList())
            }).isDisposed
        return result
    }


    fun MarkAsUnReadMessage(){
        repo.MarkAsUnReadMessage()
            .subscribe({
            },{
            }).isDisposed
    }




}