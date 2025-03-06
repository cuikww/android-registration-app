package mobile.tugas_1.registrasi

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RegistrationApiService {
    @POST("auth/customer/signup")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}