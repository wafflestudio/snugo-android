package com.wafflestudio.snugo.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wafflestudio.snugo.models.Building
import com.wafflestudio.snugo.models.Section
import com.wafflestudio.snugo.repository.BuildingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val buildingsRepository: BuildingsRepository,
) : ViewModel() {

    private val _buildings = MutableStateFlow<List<Building>>(emptyList())
    val buildingsBySection: StateFlow<Map<Section, List<Building>>> = _buildings.map {
        it.groupBy { building -> building.section }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = emptyMap())

    init {
        viewModelScope.launch {
            getBuildings()
        }
    }

    suspend fun getBuildings() {
        _buildings.value = buildingsRepository.getBuildings()
    }
}