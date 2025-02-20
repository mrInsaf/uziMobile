package com.example.uzi.ui.components.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.uzi.data.models.basic.Node
import com.example.uzi.ui.theme.Paddings

@Composable
fun DiagnosticListItem(
    date: String,
    clinic: String,
    formations: List<Node>,
    modifier: Modifier = Modifier
) {
    BasicContainer(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = clinic,
                style = MaterialTheme.typography.bodyLarge
            )
            formations.forEach() {
                TiradsContainer(
                    formationClass = it.formationClass,
                    formationProbability = it.maxTirads.times(100).toInt(),
                )
            }
        }

    }
}

