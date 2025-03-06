package mobile.tugas_1.registrasi

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String
)