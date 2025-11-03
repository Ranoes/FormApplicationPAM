package za.mobile.formapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MahasiswaForm() {
    val viewModel: MahasiswaViewModel = viewModel()
    val formState by viewModel.formState.collectAsState()
    val submissionResult by viewModel.submissionResult.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // Tambahkan ini untuk scroll
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Form Mahasiswa",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // Input Nama Depan
        Column {
            Text(
                text = "Nama Depan",
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedTextField(
                value = formState.namaDepan,
                onValueChange = viewModel::updateNamaDepan,
                modifier = Modifier.fillMaxWidth(),
                isError = formState.namaDepanError != null,
                singleLine = true
            )
            if (formState.namaDepanError != null) {
                Text(
                    text = formState.namaDepanError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Input Nama Belakang
        Column {
            Text(
                text = "Nama Belakang",
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedTextField(
                value = formState.namaBelakang,
                onValueChange = viewModel::updateNamaBelakang,
                modifier = Modifier.fillMaxWidth(),
                isError = formState.namaBelakangError != null,
                singleLine = true
            )
            if (formState.namaBelakangError != null) {
                Text(
                    text = formState.namaBelakangError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Input NIM
        Column {
            Text(
                text = "NIM",
                style = MaterialTheme.typography.bodyMedium
            )
            OutlinedTextField(
                value = formState.nim,
                onValueChange = viewModel::updateNim,
                modifier = Modifier.fillMaxWidth(),
                isError = formState.nimError != null,
                singleLine = true
            )
            if (formState.nimError != null) {
                Text(
                    text = formState.nimError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        // Preview Nama Lengkap
        if (formState.namaDepan.isNotBlank() || formState.namaBelakang.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = "Nama Lengkap:",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${formState.namaDepan} ${formState.namaBelakang}".trim(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Tombol
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { viewModel.submitForm() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Submit")
            }

            OutlinedButton(
                onClick = { viewModel.resetForm() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }
        }

        // Hasil
        submissionResult?.let { result ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Hasil:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = result,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Status Validasi
        val isValid = formState.namaDepanError == null &&
                formState.namaBelakangError == null &&
                formState.nimError == null &&
                formState.namaDepan.isNotBlank() &&
                formState.namaBelakang.isNotBlank() &&
                formState.nim.isNotBlank()

        Text(
            text = if (isValid) "Form valid" else "Form belum valid",
            color = if (isValid) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )

        // Tambahkan spacer untuk memastikan ada ruang di bawah saat scroll
        Spacer(modifier = Modifier.height(8.dp))
    }
}