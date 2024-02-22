import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R
import com.example.gymtracker.network.dto.UserWithPerformance
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID

class UserAdapter(private var users: List<UserWithPerformance>, private val onUserClicked: (UserWithPerformance) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var selectedUserId: UUID? = null
    fun getSelectedUser(): UserWithPerformance? = users.find { it.user.userId == selectedUserId }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val ivSelectedIcon: ImageView = itemView.findViewById(R.id.ivSelectedIcon)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(up: UserWithPerformance, isSelected: Boolean) {
            tvUserName.text = "${up.user.firstName} ${up.user.lastName}"
            tvSubtitle.text = up.maxPerformance?.let { performance ->
                val metricText = "${performance.performanceMetric} ${performance.unit}"
                val timeAgoText = getRelativeTimeString(performance.createdDate)
                "$metricText, $timeAgoText"
            } ?: "N/A"

            ivSelectedIcon.visibility = if (isSelected) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                selectedUserId = up.user.userId
                notifyDataSetChanged()
                onUserClicked(up)
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun getRelativeTimeString(createdDate: LocalDateTime): String {
        val now = LocalDateTime.now()
        val daysBetween = ChronoUnit.DAYS.between(createdDate, now)
        return when {
            daysBetween == 0L -> "today"
            daysBetween == 1L -> "$daysBetween day ago" // Singular for 1 day
            daysBetween in 2..6 -> "$daysBetween days ago"
            daysBetween / 7 == 1L -> "${daysBetween / 7} week ago" // Singular for 1 week
            daysBetween in 8..29 -> "${daysBetween / 7} weeks ago"
            daysBetween / 30 == 1L -> "${daysBetween / 30} month ago" // Singular for 1 month
            daysBetween in 31..364 -> "${daysBetween / 30} months ago"
            daysBetween / 365 == 1L -> "${daysBetween / 365} year ago" // Singular for 1 year
            else -> "${daysBetween / 365} years ago"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return UserViewHolder(view)
    }

    fun updateUsers(newUsers: List<UserWithPerformance>) {
        this.users = newUsers
        notifyDataSetChanged()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val up = users[position]
        holder.bind(up, up.user.userId == selectedUserId)
    }

    override fun getItemCount() = users.size
    }
