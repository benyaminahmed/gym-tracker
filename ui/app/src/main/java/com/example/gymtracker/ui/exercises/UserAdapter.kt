import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gymtracker.R
import com.example.gymtracker.network.User
import java.util.UUID

class UserAdapter(private var users: List<User>, private val onUserClicked: (User) -> Unit) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var selectedUserId: UUID? = null
    fun getSelectedUser(): User? = users.find { it.userId == selectedUserId }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userName: TextView = itemView.findViewById(R.id.tvUserName)

        private val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        private val ivSelectedIcon: ImageView = itemView.findViewById(R.id.ivSelectedIcon)


        fun bind(user: User, isSelected: Boolean) {
            tvUserName.text = "${user.firstName} ${user.lastName}"
            ivSelectedIcon.visibility = if (isSelected) View.VISIBLE else View.GONE

            itemView.setOnClickListener {
                selectedUserId = user.userId
                notifyDataSetChanged()
                onUserClicked(user)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list_item, parent, false)
        return UserViewHolder(view)
    }

    fun updateUsers(newUsers: List<User>) {
        this.users = newUsers
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user, user.userId == selectedUserId)
    }

    override fun getItemCount() = users.size
    }
