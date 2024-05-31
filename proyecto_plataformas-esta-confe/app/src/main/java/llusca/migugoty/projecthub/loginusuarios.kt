package llusca.migugoty.projecthub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class loginusuarios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_loginusuarios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button = findViewById<Button>(R.id.btnlogino)
        button.setOnClickListener {
            val intent= Intent(this, iniciooptions::class.java)
            startActivity(intent)
        }
        val incogbutton = findViewById<ImageView>(R.id.imgincog)
        incogbutton.setOnClickListener {
            val intent= Intent(this, iniciooptions::class.java)
            startActivity(intent)
        }

    }
}