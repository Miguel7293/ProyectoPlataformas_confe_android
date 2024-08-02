package llusca.migugoty.projecthub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class iniciooptions : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_iniciooptions)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val button = findViewById<Button>(R.id.btn_musica)
        button.setOnClickListener {
            val intent= Intent(this, musicainicio::class.java)
            startActivity(intent)
        }
        val button1 = findViewById<Button>(R.id.btn_horario)
        button1.setOnClickListener {
            val intent= Intent(this, horarioinicio::class.java)
            startActivity(intent)
        }
        val button2 = findViewById<Button>(R.id.btn_voz)
        button2.setOnClickListener {
            val intent= Intent(this, vozinicio::class.java)
            startActivity(intent)
        }
    }
}