package za.mobile.formapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MahasiswaViewModel : ViewModel() {

    private val _formState = MutableStateFlow(MahasiswaFormState())
    val formState: StateFlow<MahasiswaFormState> = _formState.asStateFlow()

    private val _submissionResult = MutableStateFlow<String?>(null)
    val submissionResult: StateFlow<String?> = _submissionResult.asStateFlow()

    fun updateNamaDepan(namaDepan: String) {
        val error = if (namaDepan.isBlank()) "Wajib diisi" else null
        _formState.value = _formState.value.copy(
            namaDepan = namaDepan,
            namaDepanError = error
        )
    }

    fun updateNamaBelakang(namaBelakang: String) {
        val error = if (namaBelakang.isBlank()) "Wajib diisi" else null
        _formState.value = _formState.value.copy(
            namaBelakang = namaBelakang,
            namaBelakangError = error
        )
    }

    fun updateNim(nim: String) {
        val error = when {
            nim.isBlank() -> "Wajib diisi"
            !nim.matches(Regex("\\d+")) -> "Harus angka"
            nim.length < 10 -> "Minimal 10 digit"
            else -> null
        }
        _formState.value = _formState.value.copy(
            nim = nim,
            nimError = error
        )
    }

    fun submitForm() {
        viewModelScope.launch {
            val state = _formState.value

            // Validasi sederhana
            val hasError = state.namaDepanError != null ||
                    state.namaBelakangError != null ||
                    state.nimError != null

            if (hasError) {
                _submissionResult.value = "Ada error, periksa form"
            } else {
                val namaLengkap = "${state.namaDepan} ${state.namaBelakang}"
                val mahasiswaData = MahasiswaData(namaLengkap, state.nim)

                _submissionResult.value = """
                    Data berhasil disimpan
                    
                    Nama: $namaLengkap
                    NIM: ${state.nim}
                """.trimIndent()
            }
        }
    }

    fun resetForm() {
        _formState.value = MahasiswaFormState()
        _submissionResult.value = null
    }
}