package com.waymate.app.data.repository

import com.waymate.app.data.model.PoolItem
import com.waymate.app.data.model.PoolPassenger
import com.waymate.app.data.model.PoolType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface PoolRepository {
    val pools: StateFlow<List<PoolItem>>
    suspend fun getPoolById(id: String): PoolItem?
    suspend fun joinPool(poolId: String, seatsCount: Int): Boolean
    suspend fun createPool(newPool: PoolItem): PoolItem
    suspend fun getMyHostedPools(): List<PoolItem>
    suspend fun getMyJoinedPools(): List<PoolItem>
}

class MockPoolRepository : PoolRepository {

    private val initialPools = listOf(
        PoolItem(
            id = "pool_101",
            host = PoolPassenger(
                id = "p_1",
                name = "Priya Sharma",
                avatarUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80",
                isHost = true,
                isVerified = true
            ),
            pickup = "IGI Airport Terminal 3",
            destination = "Cyber Hub, Gurugram",
            date = "Today, Nov 7",
            time = "14:30",
            estimatedArrival = "15:15",
            totalSeats = 4,
            availableSeats = 2,
            farePerPerson = 280.0,
            regularSoloFare = 780.0,
            poolType = PoolType.WOMEN_ONLY,
            passengers = listOf(
                PoolPassenger("p_1", "Priya Sharma (Host)", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=300&q=80", isHost = true),
                PoolPassenger("p_2", "Ananya Sen", "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=300&q=80")
            ),
            description = "Women-only shared cab from T3 to DLF Phase 2 / Cyber Hub. Safe & verified corporate travellers.",
            isJoinedByMe = false
        ),
        PoolItem(
            id = "pool_102",
            host = PoolPassenger(
                id = "p_3",
                name = "Vikram Malhotra",
                avatarUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80",
                isHost = true,
                isVerified = true
            ),
            pickup = "Kempegowda Airport (BLR)",
            destination = "Indiranagar, 100ft Road",
            date = "Today, Nov 7",
            time = "16:00",
            estimatedArrival = "17:20",
            totalSeats = 4,
            availableSeats = 1,
            farePerPerson = 390.0,
            regularSoloFare = 1150.0,
            poolType = PoolType.MIXED,
            passengers = listOf(
                PoolPassenger("p_3", "Vikram M. (Host)", "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=300&q=80", isHost = true),
                PoolPassenger("p_4", "Kavita Reddy", "https://images.unsplash.com/photo-1544005313-94ddf0286df2?auto=format&fit=crop&w=300&q=80"),
                PoolPassenger("p_5", "Sameer Gupta", "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?auto=format&fit=crop&w=300&q=80")
            ),
            description = "Airport express transfer heading towards East Bengaluru. AC Sedan with boot space.",
            isJoinedByMe = true
        ),
        PoolItem(
            id = "pool_103",
            host = PoolPassenger(
                id = "p_6",
                name = "Meera Nair",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80",
                isHost = true,
                isVerified = true
            ),
            pickup = "Terminal 1, IGI Airport (DEL)",
            destination = "Noida Sector 62",
            date = "Tomorrow, Nov 8",
            time = "11:00",
            estimatedArrival = "12:15",
            totalSeats = 3,
            availableSeats = 2,
            farePerPerson = 320.0,
            regularSoloFare = 890.0,
            poolType = PoolType.WOMEN_ONLY,
            passengers = listOf(
                PoolPassenger("p_6", "Meera Nair (Host)", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=300&q=80", isHost = true)
            ),
            description = "Quiet ride directly via Barapullah to Noida. 1 medium suitcase per person welcome.",
            isJoinedByMe = false
        ),
        PoolItem(
            id = "pool_104",
            host = PoolPassenger(
                id = "p_me",
                name = "Alex Morgan",
                avatarUrl = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                isHost = true,
                isVerified = true
            ),
            pickup = "Terminal 3 Arrivals",
            destination = "South Extension Part 2, New Delhi",
            date = "Tomorrow, Nov 8",
            time = "18:30",
            estimatedArrival = "19:30",
            totalSeats = 4,
            availableSeats = 3,
            farePerPerson = 250.0,
            regularSoloFare = 650.0,
            poolType = PoolType.MIXED,
            passengers = listOf(
                PoolPassenger("p_me", "Alex Morgan (Host)", "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80", isHost = true)
            ),
            description = "Hosting ride back home after return flight. Direct drop-offs along Ring Road.",
            isJoinedByMe = false,
            isHostedByMe = true
        )
    )

    private val _pools = MutableStateFlow(initialPools)
    override val pools: StateFlow<List<PoolItem>> = _pools.asStateFlow()

    override suspend fun getPoolById(id: String): PoolItem? {
        return _pools.value.find { it.id == id }
    }

    override suspend fun joinPool(poolId: String, seatsCount: Int): Boolean {
        delay(600)
        val currentList = _pools.value.toMutableList()
        val index = currentList.indexOfFirst { it.id == poolId }
        if (index != -1) {
            val pool = currentList[index]
            val newAvailable = (pool.availableSeats - seatsCount).coerceAtLeast(0)
            currentList[index] = pool.copy(
                availableSeats = newAvailable,
                isJoinedByMe = true
            )
            _pools.value = currentList
            return true
        }
        return false
    }

    override suspend fun createPool(newPool: PoolItem): PoolItem {
        delay(800)
        val list = _pools.value.toMutableList()
        list.add(0, newPool.copy(isHostedByMe = true))
        _pools.value = list
        return newPool
    }

    override suspend fun getMyHostedPools(): List<PoolItem> {
        return _pools.value.filter { it.isHostedByMe }
    }

    override suspend fun getMyJoinedPools(): List<PoolItem> {
        return _pools.value.filter { it.isJoinedByMe }
    }
}
