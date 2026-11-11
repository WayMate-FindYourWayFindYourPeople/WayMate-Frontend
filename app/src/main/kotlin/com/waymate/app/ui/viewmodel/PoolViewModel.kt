package com.waymate.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waymate.app.data.model.*
import com.waymate.app.data.repository.PoolRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PoolUiState(
    // Filter
    val selectedFilter: PoolType = PoolType.ALL,
    val pools: List<PoolItem> = emptyList(),
    val filteredPools: List<PoolItem> = emptyList(),
    val selectedPool: PoolItem? = null,

    // My Pools
    val isHostingTabSelected: Boolean = false,
    val myHostedPools: List<PoolItem> = emptyList(),
    val myJoinedPools: List<PoolItem> = emptyList(),

    // Create Pool Form
    val createPickup: String = "Terminal 3, IGI Airport (DEL)",
    val createDestination: String = "Sector 54, Golf Course Road, Gurugram",
    val createDate: String = "Today, Nov 7",
    val createTime: String = "17:00",
    val createAvailableSeats: Int = 3,
    val createPoolType: PoolType = PoolType.MIXED,
    val createEstimatedFarePerSeat: Double = 260.0,
    val isCreating: Boolean = false,
    val isSuccessCreated: Boolean = false
)

class PoolViewModel(
    private val poolRepository: PoolRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PoolUiState())
    val uiState: StateFlow<PoolUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            poolRepository.pools.collect { list ->
                _uiState.value = _uiState.value.copy(
                    pools = list,
                    filteredPools = filterList(list, _uiState.value.selectedFilter)
                )
                refreshMyPools()
            }
        }
    }

    private fun filterList(list: List<PoolItem>, filter: PoolType): List<PoolItem> {
        return when (filter) {
            PoolType.ALL -> list
            PoolType.WOMEN_ONLY -> list.filter { it.poolType == PoolType.WOMEN_ONLY }
            PoolType.MIXED -> list.filter { it.poolType == PoolType.MIXED }
        }
    }

    fun setFilter(filter: PoolType) {
        _uiState.value = _uiState.value.copy(
            selectedFilter = filter,
            filteredPools = filterList(_uiState.value.pools, filter)
        )
    }

    fun selectPool(pool: PoolItem) {
        _uiState.value = _uiState.value.copy(selectedPool = pool)
    }

    fun joinSelectedPool(seats: Int = 1, onSuccess: () -> Unit) {
        val pool = _uiState.value.selectedPool ?: return
        viewModelScope.launch {
            val success = poolRepository.joinPool(pool.id, seats)
            if (success) {
                refreshMyPools()
                onSuccess()
            }
        }
    }

    private suspend fun refreshMyPools() {
        val hosted = poolRepository.getMyHostedPools()
        val joined = poolRepository.getMyJoinedPools()
        _uiState.value = _uiState.value.copy(
            myHostedPools = hosted,
            myJoinedPools = joined
        )
    }

    fun setMyPoolsTab(isHosting: Boolean) {
        _uiState.value = _uiState.value.copy(isHostingTabSelected = isHosting)
    }

    // Create Pool handlers
    fun onCreatePickupChange(pickup: String) {
        _uiState.value = _uiState.value.copy(createPickup = pickup)
    }

    fun onCreateDestinationChange(destination: String) {
        _uiState.value = _uiState.value.copy(createDestination = destination)
    }

    fun onCreateDateChange(date: String) {
        _uiState.value = _uiState.value.copy(createDate = date)
    }

    fun onCreateTimeChange(time: String) {
        _uiState.value = _uiState.value.copy(createTime = time)
    }

    fun onCreateSeatsChange(seats: Int) {
        _uiState.value = _uiState.value.copy(createAvailableSeats = seats)
    }

    fun onCreatePoolTypeChange(type: PoolType) {
        _uiState.value = _uiState.value.copy(createPoolType = type)
    }

    fun submitCreatePool(onSuccess: () -> Unit) {
        val state = _uiState.value
        val newPool = PoolItem(
            id = "pool_${System.currentTimeMillis() % 10000}",
            host = PoolPassenger(
                id = "p_me",
                name = "Alex Morgan",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                isHost = true,
                isVerified = true
            ),
            pickup = state.createPickup,
            destination = state.createDestination,
            date = state.createDate,
            time = state.createTime,
            estimatedArrival = "18:00",
            totalSeats = state.createAvailableSeats + 1,
            availableSeats = state.createAvailableSeats,
            farePerPerson = state.createEstimatedFarePerSeat,
            regularSoloFare = state.createEstimatedFarePerSeat * 2.8,
            poolType = state.createPoolType,
            passengers = listOf(
                PoolPassenger("p_me", "Alex Morgan (Host)", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80", isHost = true)
            ),
            description = "Shared ride created via WayMate Pool. Safe & verified travellers.",
            isJoinedByMe = false,
            isHostedByMe = true
        )

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreating = true)
            poolRepository.createPool(newPool)
            refreshMyPools()
            _uiState.value = _uiState.value.copy(
                isCreating = false,
                isSuccessCreated = true
            )
            onSuccess()
        }
    }
}
