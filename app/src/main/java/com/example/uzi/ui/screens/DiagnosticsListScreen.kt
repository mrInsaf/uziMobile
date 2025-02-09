package com.example.uzi.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.uzi.data.models.networkResponses.Uzi
import com.example.uzi.data.models.networkResponses.UziListResponse
import com.example.uzi.ui.components.containers.DiagnosticListItem
import com.example.uzi.ui.theme.Paddings

@Composable
fun DiagnosticsListScreen(
    uziList: List<Uzi>,
    onDiagnosticListItemClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "История диагностик",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(Modifier.size(Paddings.Large))
        LazyColumn {
            items(uziList) { uzi ->
                DiagnosticListItem(
                    date = uzi.createAt ?: "Неизвестная дата",
                    clinic = "Неизвестная клиника",
                    formations = emptyList(),
                    modifier = Modifier
                        .clickable {
                            onDiagnosticListItemClick(uzi.id, uzi.createAt,)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun DiagnosticsListScreenPreview() {
    DiagnosticsListScreen(
        uziList = emptyList(),
        onDiagnosticListItemClick = {_, _ -> }
    )
}