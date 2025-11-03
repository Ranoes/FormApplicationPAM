package za.mobile.formapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class `MahasiswaViewModel.kt` : ViewModel() {

    private val _formState = MutableStateFlow(MahasiswaFormState())
    val formState: StateFlow<MahasiswaFormState> = _formState.asStateFlow()

    private val _submissionResult = MutableStateFlow<String?>(null)
    val submissionResult: StateFlow<String?> = _submissionResult.asStateFlow()

    private val _validationHistory = MutableStateFlow<List<String>>(emptyList())
    val validationHistory: StateFlow<List<String>> = _validationHistory.asStateFlow()

    fun updateNamaDepan(namaDepan: String) {
        _formState.value = _formState.value.copy(
            namaDepan = namaDepan,
            namaDepanError = validateNamaDepan(namaDepan)
        )
        addToHistory("Nama Depan: $namaDepan")
    }

    fun updateNamaBelakang(namaBelakang: String) {
        _formState.value = _formState.value.copy(
            namaBelakang = namaBelakang,
            namaBelakangError = validateNamaBelakang(namaBelakang)
        )
        addToHistory("Nama Belakang: $namaBelakang")
    }

    fun updateNim(nim: String) {
        _formState.value = _formState.value.copy(
            nim = nim,
            nimError = validateNim(nim)
        )
        addToHistory("NIM: $nim")
    }

    private fun validateNamaDepan(nama: String): String? = when {
        nama.isBlank() -> "Nama depan tidak boleh kosong"
        nama.length < 2 -> "Nama depan terlalu pendek"
        !nama.matches(Regex("[a-zA-Z ]+")) -> "Nama depan hanya boleh mengandung huruf"
        else -> null
    }

    private fun validateNamaBelakang(nama: String): String? = when {
        nama.isBlank() -> "Nama belakang tidak boleh kosong"
        nama.length < 2 -> "Nama belakang terlalu pendek"
        !nama.matches(Regex("[a-zA-Z ]+")) -> "Nama belakang hanya boleh mengandung huruf"
        else -> null
    }

    private fun validateNim(nim: String): String? = when {
        nim.isBlank() -> "NIM tidak boleh kosong"
        !nim.matches(Regex("\\d{10,15}")) -> "NIM harus 10-15 digit angka"
        else -> null
    }

    fun validateForm(): Boolean {
        val currentState = _formState.value

        val errors = listOf(
            validateNamaDepan(currentState.namaDepan),
            validateNamaBelakang(currentState.namaBelakang),
            validateNim(currentState.nim)
        )

        val isValid = errors.all { it == null }

        if (isValid) {
            addToHistory("Form valid!")
        } else {
            addToHistory("Form tidak valid!")
        }

        return isValid
    }

    fun submitForm() {
        viewModelScope.launch {
            val submissionStatus = when {
                validateForm() -> {
                    val state = _formState.value

                    val mahasiswaData = MahasiswaData(
                        namaLengkap = "${state.namaDepan} ${state.namaBelakang}".trim(),
                        nim = state.nim
                    )

                    processSubmission(mahasiswaData)
                    "SUCCESS"
                }
                else -> "FAILED"
            }

            addToHistory("Submission: $submissionStatus")
        }
    }

    private fun processSubmission(data: MahasiswaData) {
        val statusMessage = when (data.namaLengkap.length) {
            in 1..10 -> "Nama pendek"
            in 11..20 -> "Nama medium"
            else -> "Nama panjang"
        }

        val result = """
            Data berhasil disubmit!

            Detail Mahasiswa:
            Nama Lengkap: ${data.namaLengkap} ($statusMessage)
            NIM: ${data.nim}

            Data telah digabungkan dari:
            - Nama Depan: ${_formState.value.namaDepan}
            - Nama Belakang: ${_formState.value.namaBelakang}

            Informasi:
            ${getAdditionalInfo(data)}
        """.trimIndent()

        _submissionResult.value = result
    }

    private fun getAdditionalInfo(data: MahasiswaData): String {
        val infoList = listOf(
            "Panjang nama: ${data.namaLengkap.length} karakter",
            "NIM valid: ${data.nim.length} digit",
            "Inisial: ${getInitials(data.namaLengkap)}"
        )

        return infoList.joinToString("\n") { item -> "• $item" }
    }

    private fun getInitials(nama: String): String {
        return nama.split(" ").joinToString("") { word -> word.firstOrNull()?.uppercase() ?: "" }
    }

    private fun addToHistory(message: String) {
        _validationHistory.value = _validationHistory.value +
                listOf("${System.currentTimeMillis() % 10000}: $message")
    }

    fun clearSubmissionResult() {
        _submissionResult.value = null
    }

    fun resetForm() {
        _formState.value = MahasiswaFormState()
        _submissionResult.value = null
        _validationHistory.value = emptyList()
        addToHistory("Form direset")
    }
}