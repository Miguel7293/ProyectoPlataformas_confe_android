package llusca.migugoty.projecthub

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DailyActivitiesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var switchViewButton: Button
    private lateinit var addActivityButton: Button
    private lateinit var activityList: MutableList<ActivityItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_daily_activities)

        // Set insets for the main layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize RecyclerView and buttons
        recyclerView = findViewById(R.id.recyclerView)
        switchViewButton = findViewById(R.id.switchViewButton)
        addActivityButton = findViewById(R.id.addActivityButton)

        // Setup RecyclerView
        activityList = mutableListOf()
        activityAdapter = ActivityAdapter(activityList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = activityAdapter

        // Set listeners for buttons
        switchViewButton.setOnClickListener {
            // Logic to switch view
        }

        addActivityButton.setOnClickListener {
            // Logic to add a new activity
        }

        // Get the selected date from the intent
        val selectedDate = intent.getStringExtra("selectedDate")
        // Load activities for the selected date (implement this logic as needed)
        loadActivitiesForDate(selectedDate)
    }

    private fun loadActivitiesForDate(date: String?) {
        // Example logic to load activities for the selected date
        // Replace with actual implementation
        if (date != null) {
            // Add some dummy data
            activityList.add(ActivityItem("Gimnasio", "7am-10am", "Prioritario"))
            activityList.add(ActivityItem("Practicar cálculo", "8pm-10pm", "Pasable"))
            // Notify adapter about the data change
            activityAdapter.notifyDataSetChanged()
        }
    }
}
