package com.example.uzi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.uzi.ui.components.DateFormField
import com.example.uzi.ui.screens.MainScreen
import com.example.uzi.ui.theme.UziTheme
import com.example.uzi.ui.viewModel.authorisation.AuthorisationViewModel
import com.example.uzi.ui.viewModel.newDiagnostic.NewDiagnosticViewModel
import com.example.uzi.ui.viewModel.registraion.RegistraionViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val authorisationViewModel = AuthorisationViewModel()
        val registrationViewModel = RegistraionViewModel()
        val newDiagnosticViewModel = NewDiagnosticViewModel()

        enableEdgeToEdge()
        setContent {
            UziTheme {
                MainScreen(
                    newDiagnosticViewModel = newDiagnosticViewModel
                )
            }
        }
    }
}