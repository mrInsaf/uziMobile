package com.example.uzi.data.repository

import com.example.uzi.data.mock.MockAuthData
import kotlinx.coroutines.delay

class MockUziServiceRepository : UziServiceRepository {
    private val authData = MockAuthData()
    override suspend fun checkAuthorisation(): Boolean {
        delay(100) // Имитация задержки
        return authData.isAuthorised
    }

    override suspend fun submitLogin(
        email: String,
        password: String
    ): Boolean {
        delay(100)
        return email == authData.email && password == authData.password
    }


    override suspend fun submitLogout(): Boolean {
        delay(100)
        authData.isAuthorised = false
        return true
    }
}
