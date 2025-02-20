package com.example.uzi.ui.viewModel.diagnosticList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrinsaf.core.data.models.basic.NodesWithUziId
import com.mrinsaf.core.data.repository.UziServiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiagnosticListViewModel @Inject constructor(
    private val repository: UziServiceRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(DiagnosticListUiState())
    val uiState: StateFlow<DiagnosticListUiState>
        get() = _uiState

    fun getPatientUzis(patientId: String) {
        viewModelScope.launch {
            val uziList = repository.getPatientUzis(patientId)
            val nodesWithUziIds = mutableListOf<NodesWithUziId>()
            uziList.forEach { uzi ->
                val id = uzi.id
                val nodesResponse = repository.getUziNodes(id)
                nodesWithUziIds.add(
                    NodesWithUziId(
                        uziId = id,
                        nodes = nodesResponse.nodes
                    )
                )
            }

            _uiState.update {
                it.copy(
                    uziList = uziList,
                    nodesWithUziId = nodesWithUziIds
                )
            }
        }
    }
}