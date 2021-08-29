package kg.timur.app.todo.fragments.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import kg.timur.app.todo.R
import kg.timur.app.todo.data.models.Priority
import kg.timur.app.todo.data.models.ToDoData
import kg.timur.app.todo.fragments.list.ListFragmentDirections
import kotlinx.android.synthetic.main.row_layout.view.*
import kotlinx.android.synthetic.main.row_layout.view.row_back
import kotlinx.android.synthetic.main.row_layout.view.subtitle_txt
import kotlinx.android.synthetic.main.row_layout.view.title_txt

class ListAdapter: RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var dataList = emptyList<ToDoData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.title_txt.text = dataList[position].title
        holder.itemView.subtitle_txt.text = dataList[position].description
        holder.itemView.row_back.setOnClickListener {
            val act = ListFragmentDirections.actionListFragmentToUpdateFragment(dataList[position])
            holder.itemView.findNavController().navigate(act)
        }

        val priority = dataList[position].priority
        when(priority){
            Priority.HIGH -> holder.itemView.priority_ind.setCardBackgroundColor(ContextCompat
                    .getColor(holder.itemView.context, R.color.red))
            Priority.MEDIUM -> holder.itemView.priority_ind.setCardBackgroundColor(ContextCompat
                    .getColor(holder.itemView.context, R.color.yellow))
            Priority.LOW -> holder.itemView.priority_ind.setCardBackgroundColor(ContextCompat
                    .getColor(holder.itemView.context, R.color.green))
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    }

    fun setData(toDoData: List<ToDoData>){
        this.dataList = toDoData
        notifyDataSetChanged()
    }
}