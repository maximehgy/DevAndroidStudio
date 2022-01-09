package com.hema.todo

import android.app.Application
import com.hema.todo.network.Api

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        Api.setUpContext(this)
    }
}