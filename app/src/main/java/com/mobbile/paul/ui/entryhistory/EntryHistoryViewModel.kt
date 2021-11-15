package com.mobbile.paul.ui.entryhistory


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobbile.paul.model.Attendant
import com.mobbile.paul.model.EntityGetSalesEntry
import com.mobbile.paul.model.postToServer
import com.mobbile.paul.model.toAllOutletsList
import com.mobbile.paul.ui.entryhistory.repository.EntryRepository
import com.mobbile.paul.util.Util.getTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class EntryHistoryViewModel @Inject constructor(private var repository: EntryRepository) :
    ViewModel() {

    private val _entriesUiState = MutableStateFlow<PostEntryUiState<List<EntityGetSalesEntry>>>(PostEntryUiState.Empty)
    val entriesUiState get() = _entriesUiState

    fun fetchAllSalesEntries() = viewModelScope.launch {
        _entriesUiState.value = PostEntryUiState.Loading
        try {
            val data = repository.fetchAllEntryPerDailys()
            _entriesUiState.value = PostEntryUiState.Success(data)
        } catch (e: Throwable) {
            _entriesUiState.value = PostEntryUiState.Error(e.message.toString(),100)
        }
    }


    private val _postUiState = MutableStateFlow<PostEntryUiState<Attendant>>(PostEntryUiState.Empty)
    val postUiState get() = _postUiState

    fun fetchAllPostedEntries(
        repid: Int,
        currentlat: String,
        currentlng: String,
        outletlat: String,
        outletlng: String,
        distance: String,
        duration: String,
        urno: Int,
        visitsequence: Int,
        auto: Int,
        token: String,
        uiid: String
    ) = viewModelScope.launch {
        try {
            val isAllSalesEntry = repository.pullAllSalesEntry()

            if (isAllSalesEntry.isNotEmpty()) {

                val list = postToServer()
                list.repid = repid
                list.currentlat = currentlat
                list.currentlng = currentlng
                list.outletlat = outletlat
                list.outletlng = outletlng
                list.arrivaltime = getTime()
                list.visitsequence = visitsequence
                list.distance = distance
                list.duration = duration
                list.urno = urno
                list.token = token
                list.uiid = uiid
                list.lists = isAllSalesEntry.map {it.toAllOutletsList()}

                try {
                    val resultFromPost = repository.postSales(list)
                    _postUiState.value = PostEntryUiState.Success(resultFromPost)

                }catch (e: Throwable) {
                    _postUiState.value = PostEntryUiState.Error(e.message.toString(), 400)
                }
                return@launch
            }

        } catch (e: Throwable) {
            _postUiState.value = PostEntryUiState.Error(e.message.toString(), 200)
        }
    }

}