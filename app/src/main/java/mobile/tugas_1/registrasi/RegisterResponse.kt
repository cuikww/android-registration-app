package mobile.tugas_1.registrasi

data class RegisterResponse(
    val success: Boolean,
    val message: String,
    val user: User?
)