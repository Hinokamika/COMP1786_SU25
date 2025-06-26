package com.example.comp1786_su25.controllers

import com.example.comp1786_su25.dataClasses.userModel
import com.google.firebase.database.FirebaseDatabase

object userFirebaseRepository {

    private val db = FirebaseDatabase.getInstance().getReference("users")

    fun addUser(user: userModel) {
        val userId = db.push().key ?: return
        db.child(userId).setValue(user.copy(id = userId))
    }

    fun getUsers(callback: (List<userModel>) -> Unit) {
        db.get().addOnSuccessListener { snapshot ->
            val users = snapshot.children.mapNotNull { it.getValue(userModel::class.java) }
            callback(users)
        }.addOnFailureListener {
            callback(emptyList())
        }
    }

    fun getUserById(userId: String, callback: (userModel?) -> Unit) {
        db.child(userId).get().addOnSuccessListener { snapshot ->
            val userData = snapshot.getValue(userModel::class.java)
            callback(userData)
        }.addOnFailureListener {
            callback(null)
        }
    }

    fun updateUser(user: userModel) {
        user.id?.let { id ->
            db.child(id).setValue(user)
        }
    }

    fun deleteUser(userId: String) {
        db.child(userId).removeValue()
    }
}