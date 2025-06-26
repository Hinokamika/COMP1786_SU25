package com.example.comp1786_su25.functionPages.User

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.comp1786_su25.components.DetailItem
import com.example.comp1786_su25.components.DetailSection
import com.example.comp1786_su25.controllers.classFirebaseRepository
import com.example.comp1786_su25.controllers.userFirebaseRepository
import com.example.comp1786_su25.dataClasses.userModel

@Composable
fun UserDetailsDialog(modifier: Modifier = Modifier, userData: userModel, onDismiss: () -> Unit, navController: NavController) {
    var userClasses by remember { mutableStateOf(listOf<Pair<String, String>>()) }

    // Print out user details for debugging
    LaunchedEffect(Unit) {
        println("DEBUG: UserDetailsDialog opened with user:")
        println("DEBUG: ID: ${userData.id}")
        println("DEBUG: Name: ${userData.name}")
        println("DEBUG: Fields: ${userData.email}, ${userData.phone}, ${userData.age}")
    }

    fun refreshUserClasses() {
        val userId = userData.id ?: ""
        println("DEBUG: Looking for classes with userId: $userId")

        classFirebaseRepository.getClassesByTeacherId(userId) { classes ->
            println("DEBUG: Found ${classes.size} classes for user")
            if (classes.isNotEmpty()) {
                println("DEBUG: First class: ${classes[0].class_name}, teacher: ${classes[0].teacher}")
            }
            userClasses = classes.map { it.class_name to it.type_of_class }
        }
    }
    // Always refresh when dialog is shown
    LaunchedEffect(Unit) {
        refreshUserClasses()
    }
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Text(
                    text = userData.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                // User Identity Section
                DetailSection(title = "User Info", content = {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DetailItem(label = "Email", value = userData.email)
                            DetailItem(label = "Phone", value = userData.phone)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            DetailItem(label = "Age", value = userData.age)
                        }
                    }
                })
                // Classes Taught Section
                DetailSection(title = "Classes Taught", content = {
                    if (userClasses.isEmpty()) {
                        Text("No classes found", style = MaterialTheme.typography.bodyMedium)
                    } else {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            userClasses.forEach { (className, typeOfClass) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Class: $className", style = MaterialTheme.typography.bodyMedium)
                                    Text("Type: $typeOfClass", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                        }
                    }
                })

                Button(
                    onClick = {
                        // Navigate to update screen with user ID
                        userData.id?.let { id ->
                            navController.navigate("updateuser/${id}")
                            onDismiss() // Close the dialog after navigation
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Update User")
                }

                Button(onClick = {
                    userData.id?.let { id ->
                        // Call the delete function from the repository
                        userFirebaseRepository.deleteUser(id)
                        // Dismiss the dialog
                        onDismiss()
                        // Navigate back to refresh the user list
                        navController.popBackStack()
                    }
                },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Delete User")
                }

                // Close button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }
}