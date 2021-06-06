package not.working.code.ican.app.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import not.working.code.ican.R
import not.working.code.ican.data.TaskList
import not.working.code.ican.utils.enums.TaskStatusEnum

class TaskListAdapter(val click: (id: Int) -> Unit) : RecyclerView.Adapter<TaskListAdapter.TaskListVH>() {

    private val taskList = ArrayList<TaskList>()
    private val taskListFull = ArrayList<TaskList>()

    class TaskListVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.task_title)
        val date = itemView.findViewById<TextView>(R.id.task_time_date)
        val executor = itemView.findViewById<TextView>(R.id.task_executors)
        val status = itemView.findViewById<TextView>(R.id.task_status)
        val dateStatus = itemView.findViewById<View>(R.id.task_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskListVH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskListVH(itemView = view)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    override fun onBindViewHolder(holder: TaskListVH, position: Int) {
        val item = taskList[position]
        holder.apply {
            date.text = item.deadline
            status.text = item.status.rus
            title.text = item.title
            executor.text = item.executor

            when(item.dayToDeadline) {
                in 0..2 -> dateStatus.setBackgroundColor(itemView.context.getColor(R.color.chip_red))
                in 3..5 -> dateStatus.setBackgroundColor(itemView.context.getColor(R.color.chip_orange))
                else -> dateStatus.setBackgroundColor(itemView.context.getColor(R.color.chip_green))
            }
            itemView.setOnClickListener {
                click(item.id)
            }
        }
    }

    fun onFilter(status: TaskStatusEnum) {
        taskList.clear()

        taskListFull.forEach { task ->
            if (task.status == status) {
                taskList.add(task)
            }
        }
        notifyDataSetChanged()
    }

    fun offFilter() {
        taskList.clear()
        taskList.addAll(taskListFull)
        notifyDataSetChanged()
    }

    fun changeTaskList(tasks: ArrayList<TaskList>) {
        Log.e("tasks", tasks.size.toString())
        taskListFull.clear()
        taskListFull.addAll(tasks)
        offFilter()
    }
}