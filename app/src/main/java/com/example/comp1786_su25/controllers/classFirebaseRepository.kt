package com.example.comp1786_su25.controllers

import com.example.comp1786_su25.dataClasses.classModel
import com.google.firebase.database.FirebaseDatabase

object classFirebaseRepository {
    private val db = FirebaseDatabase.getInstance().getReference("classes")

    fun addClass(classModel: classModel) {
        val classId = db.push().key ?: return
        db.child(classId).setValue(classModel.copy(id = classId))
    }

    fun getClasses(callback: (List<classModel>) -> Unit) {
        db.get().addOnSuccessListener { snapshot ->
            val classes = snapshot.children.mapNotNull { it.getValue(classModel::class.java) }
            callback(classes)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }
}