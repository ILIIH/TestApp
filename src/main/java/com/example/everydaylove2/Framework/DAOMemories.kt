package com.example.everydaylove2.Framework

import androidx.lifecycle.MutableLiveData
import com.example.everydaylove2.domain.Memory
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*
import javax.inject.Inject

class DAOMemories @Inject constructor(val DB: FirebaseFirestore) {

    var loaded = MutableLiveData<Boolean>()
    var memories = ArrayList<Memory>()

    fun add(memory: Memory): Task<DocumentReference> = DB.collection("Memories").add(memory)

    fun ReadAll() {

        memories.clear()
        DB.collection("Memories")
            .get()
            .addOnCompleteListener(
                OnCompleteListener<QuerySnapshot?> { task ->
                    if (task.isSuccessful) {
                        for (document in task.result) {
                            var img = document.data.get("image").toString()
                            var descr = document.data.get("description").toString()
                            var year = document.data.get("year").toString()
                            var date = document.data.get("date").toString()
                            memories.add(Memory(img, descr, year, date))
                        }
                    } else {
                    }
                }
            ).addOnSuccessListener {
                loaded.postValue(true)
            }
    }
}
