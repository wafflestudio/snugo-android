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
class HomeViewModel
    @Inject
    constructor(
        private val buildingsRepository: BuildingsRepository,
    ) : ViewModel() {
        private val buildings = MutableStateFlow<List<Building>>(emptyList())
        val buildingsBySection: StateFlow<Map<Section, List<Building>>> =
            buildings.map {
                it.groupBy { building -> building.section }
            }.stateIn(viewModelScope, SharingStarted.Eagerly, initialValue = emptyMap())

        private val _startBuilding = MutableStateFlow<Building?>(null)
        val startBuilding: StateFlow<Building?> get() = _startBuilding

        private val _endBuilding = MutableStateFlow<Building?>(null)
        val endBuilding: StateFlow<Building?> get() = _endBuilding

        init {
            viewModelScope.launch {
                getBuildings()
            }
        }

        suspend fun getBuildings() {
            buildings.value = buildingsRepository.getBuildings()
        }

        fun setStartBuilding(building: Building) {
            _startBuilding.value = building
        }

        fun setEndBuilding(building: Building) {
            _endBuilding.value = building
        }
    }
