package com.example.pureplayer1

import android.app.Application

class AppApplication : Application() {

    companion object {
        private lateinit var instance: AppApplication

        fun getInstance(): AppApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}