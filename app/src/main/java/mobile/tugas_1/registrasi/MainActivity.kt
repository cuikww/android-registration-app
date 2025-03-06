package mobile.tugas_1.registrasi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import mobile.tugas_1.registrasi.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val tag = "RegistrationActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        setupRegisterButton()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupRegisterButton() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (!validateInput(name, email, password)) return@setOnClickListener

            performRegistration(name, email, password)
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showToast("Semua kolom harus diisi!")
            return false
        }
        return true
    }

    private fun performRegistration(name: String, email: String, password: String) {
        Log.d(tag, "Melakukan registrasi: name=$name, email=$email")
        val request = RegisterRequest(email, password, name)

        RetrofitClient.apiService.register(request).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                handleRegistrationResponse(response)
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Log.e(tag, "Gagal melakukan registrasi: ${t.message}", t)
                showToast("Error: ${t.message}")
            }
        })
    }

    private fun handleRegistrationResponse(response: Response<RegisterResponse>) {
        Log.d(tag, "Response code: ${response.code()}")
        val registerResponse = response.body()

        if (registerResponse?.success == true) {
            binding.tvMessage.text = "Hello, ${registerResponse.user?.name}!"
            showToast("Registrasi berhasil")
        } else {
            val errorMessage = registerResponse?.message ?: "Registrasi gagal"
            showToast(errorMessage)
            Log.e(tag, "Error: ${response.errorBody()?.string()}")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}