package llusca.migugoty.projecthub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ActivityItem(val name: String, val time: String, val priority: String)

class ActivityAdapter(private val activityList: List<ActivityItem>) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activityItem = activityList[position]
        holder.bind(activityItem)
    }

    override fun getItemCount() = activityList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val activityName: TextView = itemView.findViewById(R.id.activityName)
        private val activityTime: TextView = itemView.findViewById(R.id.activityTime)
        private val activityPriority: TextView = itemView.findViewById(R.id.activityPriority)

        fun bind(activityItem: ActivityItem) {
            activityName.text = activityItem.name
            activityTime.text = activityItem.time
            activityPriority.text = activityItem.priority
        }
    }
}
