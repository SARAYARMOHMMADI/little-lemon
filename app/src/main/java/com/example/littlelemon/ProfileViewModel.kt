package com.example.littlelemon

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileViewModel(context: Context) : ViewModel() {

    private val sharedPref =
        context.getSharedPreferences("LittleLemon", Context.MODE_PRIVATE)


    var firstName by mutableStateOf("")
        private set

    var lastName by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var imageUri by mutableStateOf<Uri?>(null)
        private set

    init {
        loadData()
    }

    private fun loadData() {
        firstName = sharedPref.getString("firstName", "") ?: ""
        lastName = sharedPref.getString("lastName", "") ?: ""
        email = sharedPref.getString("email", "") ?: ""

        val uriString = sharedPref.getString("profileImage", null)
        imageUri = uriString?.let { Uri.parse(it) }
    }

    fun onFirstNameChange(value: String) {
        firstName = value
    }

    fun onLastNameChange(value: String) {
        lastName = value
    }

    fun onImageChange(uri: Uri) {
        imageUri = uri
    }

    fun saveProfile() {
        sharedPref.edit()
            .putString("firstName", firstName)
            .putString("lastName", lastName)
            .putString("profileImage", imageUri?.toString())
            .apply()
    }

    fun clearProfile() {
        sharedPref.edit().clear().apply()
    }
}