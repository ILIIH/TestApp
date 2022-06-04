package com.example.everydaylove2.di

import android.content.Context
import com.example.everydaylove2.Presentation.Dialog.AddMemoryDialog
import com.example.everydaylove2.Presentation.Fragments.MemoriesFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [FirebaceModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun dialogInject(app: AddMemoryDialog)
    fun dayathistoryInject(app: MemoriesFragment)
}
