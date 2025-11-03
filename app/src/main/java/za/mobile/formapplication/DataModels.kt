package za.mobile.formapplication

data class MahasiswaFormState(
    val namaDepan: String = "",
    val namaBelakang: String = "",
    val nim: String = "",
    val namaDepanError: String? = null,
    val namaBelakangError: String? = null,
    val nimError: String? = null
)

data class MahasiswaData(
    val namaLengkap: String,
    val nim: String
)