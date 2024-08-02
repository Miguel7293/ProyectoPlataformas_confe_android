package llusca.migugoty.projecthub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class loginusuarios : AppCompatActivity() {

    private lateinit var gsc: GoogleSignInClient
    private lateinit var googleBtn: ImageView
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText

    private val predefinedUsername = "admin"
    private val predefinedPassword = "password123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loginusuarios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usernameInput = findViewById(R.id.usuarioinicio)
        passwordInput = findViewById(R.id.etpassword)
        googleBtn = findViewById(R.id.imggmail)

        val button = findViewById<Button>(R.id.btnlogino)
        button.setOnClickListener {
            handleLogin()
        }

        val incogbutton = findViewById<ImageView>(R.id.imgincog)
        incogbutton.setOnClickListener {
            handleLogin()
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        gsc = GoogleSignIn.getClient(this, gso)

        googleBtn.setOnClickListener {
            signIn()
        }
    }

    private fun handleLogin() {
        val username = usernameInput.text.toString()
        val password = passwordInput.text.toString()

        if (username == predefinedUsername && password == predefinedPassword) {
            navigateToSecondActivity()
        } else {
            Toast.makeText(this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun signIn() {
        val signInIntent = gsc.signInIntent
        startActivityForResult(signInIntent, 1000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            navigateToSecondActivity()
        } catch (e: ApiException) {
            Toast.makeText(applicationContext, "Error de Inicio de sesión Con Google: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToSecondActivity() {
        val intent = Intent(this, iniciooptions::class.java)
        startActivity(intent)
        finish()
    }
}
