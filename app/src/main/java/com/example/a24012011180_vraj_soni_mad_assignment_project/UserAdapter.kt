package com.example.a24012011180_vraj_soni_mad_assignment_project

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * UserAdapter — master/displayed-list pattern for live search + row interactions.
 *
 * Row IDs in item_user.xml:
 *   avatarInitial, tvUserName, tvUserEmail, tvUserPhone, tvUserBatch, tvUserRole
 */
class UserAdapter(
    masterList: List<User>,
    private val onUserClick: (User) -> Unit,
    private val onUserLongClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val master: List<User> = masterList
    private var displayList: MutableList<User> = master.toMutableList()

    // ── Search ──────────────────────────────────────────────────────────────

    fun buildTextWatcher(): TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) = Unit
        override fun onTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) = Unit
        override fun afterTextChanged(s: Editable?) { filter(s?.toString() ?: "") }
    }

    fun filter(query: String) {
        val q = query.trim().lowercase()
        displayList = if (q.isEmpty()) master.toMutableList()
        else master.filter { u ->
            u.fullName.lowercase().contains(q) ||
            u.email.lowercase().contains(q) ||
            u.phone.lowercase().contains(q) ||
            u.batch.lowercase().contains(q) ||
            u.role.lowercase().contains(q)
        }.toMutableList()
        notifyDataSetChanged()
    }

    fun getDisplayCount(): Int = displayList.size

    // ── Adapter overrides ────────────────────────────────────────────────────

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int = displayList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = displayList[position]

        // Avatar initial
        holder.avatarInitial.text =
            user.fullName.firstOrNull()?.uppercaseChar()?.toString() ?: "U"

        // Text fields
        holder.tvName.text  = user.fullName
        holder.tvEmail.text = user.email
        holder.tvPhone.text = "${user.phone}  ·  ${user.batch}"
        holder.tvBatch.text = user.batch   // hidden view kept for search

        // Role chip: navy for ADMIN, orange for STUDENT
        val ctx = holder.itemView.context
        val isAdmin = user.role.equals("ADMIN", ignoreCase = true)
        holder.tvRole.text = if (isAdmin)
            ctx.getString(R.string.chip_admin)
        else
            ctx.getString(R.string.chip_student)

        if (isAdmin) {
            holder.tvRole.setBackgroundResource(R.drawable.bg_chip_success)
            holder.tvRole.setTextColor(ctx.getColor(R.color.colorSuccess))
        } else {
            holder.tvRole.setBackgroundResource(R.drawable.bg_chip_warning)
            holder.tvRole.setTextColor(ctx.getColor(R.color.colorWarning))
        }

        holder.itemView.setOnClickListener      { onUserClick(user) }
        holder.itemView.setOnLongClickListener  { onUserLongClick(user); true }
    }

    // ── ViewHolder ───────────────────────────────────────────────────────────

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val avatarInitial: TextView = itemView.findViewById(R.id.avatarInitial)
        val tvName:        TextView = itemView.findViewById(R.id.tvUserName)
        val tvEmail:       TextView = itemView.findViewById(R.id.tvUserEmail)
        val tvPhone:       TextView = itemView.findViewById(R.id.tvUserPhone)
        val tvBatch:       TextView = itemView.findViewById(R.id.tvUserBatch)
        val tvRole:        TextView = itemView.findViewById(R.id.tvUserRole)
    }
}
