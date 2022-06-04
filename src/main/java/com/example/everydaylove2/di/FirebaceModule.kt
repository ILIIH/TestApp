package com.example.everydaylove2.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaceModule {
    @Singleton
    @Provides
    fun provideFireStore(): FirebaseFirestore = FirebaseFirestore.getInstance()

    lateinit var Fstorage: FirebaseStorage
    lateinit var StorageReference: StorageReference

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Singleton
    @Provides
    fun provideStorageReference(fires: FirebaseStorage): StorageReference = fires.reference
}
