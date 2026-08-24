package com.aura.defense

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val PREFERENCES_NAME = "aura_defense_preferences"
private const val AURA_ID_KEY = "aura_id"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preferences = getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        val auraId = preferences.getString(AURA_ID_KEY, null) ?: createAuraId().also {
            preferences.edit().putString(AURA_ID_KEY, it).apply()
        }

        setContent {
            AuraDefenseTheme {
                var currentAuraId by rememberSaveable { mutableStateOf(auraId) }
                AuraDefenseScreen(
                    auraId = currentAuraId,
                    onAuraIdChange = { newId ->
                        currentAuraId = newId
                        preferences.edit().putString(AURA_ID_KEY, newId).apply()
                    }
                )
            }
        }
    }
}

private fun createAuraId(): String {
    val suffix = (1..6).map { "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".random() }.joinToString("")
    return "AURA-$suffix"
}

@Composable
private fun AuraDefenseScreen(auraId: String, onAuraIdChange: (String) -> Unit) {
    var score by rememberSaveable { mutableStateOf("Analizando...") }
    var showAuraCenter by rememberSaveable { mutableStateOf(false) }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF07151A))
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "AURA / 01",
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(Modifier.height(14.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.app_subtitle),
                    color = Color(0xFFA9C2C5),
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(44.dp))
                Text(
                    text = "AURA SCORE",
                    color = Color(0xFF7B979B),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = score,
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(28.dp))
                Button(
                    onClick = { score = "Base lista para diagnóstico real" },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color(0xFF052024)
                    )
                ) {
                    Text("Iniciar diagnóstico", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { showAuraCenter = true },
                    modifier = Modifier.fillMaxWidth().height(54.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Centro Aura", fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(30.dp))
                Text(
                    text = stringResource(R.string.privacy_note),
                    color = Color(0xFF789296),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }

            Text(
                text = auraId,
                modifier = Modifier.align(Alignment.BottomCenter),
                color = Color(0xFF5D777B),
                fontSize = 12.sp
            )
        }
    }

    if (showAuraCenter) {
        AuraCenterDialog(
            auraId = auraId,
            onSave = { updatedId ->
                onAuraIdChange(updatedId)
                showAuraCenter = false
            },
            onDismiss = { showAuraCenter = false }
        )
    }
}

@Composable
private fun AuraCenterDialog(auraId: String, onSave: (String) -> Unit, onDismiss: () -> Unit) {
    var editedAuraId by rememberSaveable(auraId) { mutableStateOf(auraId) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Centro Aura") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                OutlinedTextField(
                    value = editedAuraId,
                    onValueChange = { editedAuraId = it.take(32) },
                    label = { Text("Aura ID") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(18.dp)) {
                    Text("Privacy", color = MaterialTheme.colorScheme.primary)
                    Text("Terms", color = MaterialTheme.colorScheme.primary)
                }
                Text("Version 0.1.0", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(editedAuraId.ifBlank { auraId }) }) {
                Text("Guardar")
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}

@Composable
private fun AuraDefenseTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = androidx.compose.material3.darkColorScheme(
            primary = Color(0xFF5DE1D1),
            secondary = Color(0xFFE3B66B),
            background = Color(0xFF07151A),
            surface = Color(0xFF10242A),
            onBackground = Color(0xFFE7F5F3)
        ),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
private fun AuraDefensePreview() {
    AuraDefenseTheme {
        AuraDefenseScreen(auraId = "AURA-ABC123", onAuraIdChange = {})
    }
}
