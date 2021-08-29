package kg.timur.app.todo.fragments.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kg.timur.app.todo.R
import kg.timur.app.todo.data.models.ToDoData

class GroupListAdapter: RecyclerView.Adapter<GroupListAdapter.ViewHolder>() {

    var groupList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.column_item, parent, false)
        return GroupListAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return groupList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}