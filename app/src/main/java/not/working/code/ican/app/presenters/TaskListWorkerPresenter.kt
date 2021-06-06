package not.working.code.ican.app.presenters

import android.content.Context
import androidx.cardview.widget.CardView
import moxy.MvpPresenter
import not.working.code.ican.app.views.TaskListWorkerView
import not.working.code.ican.data.TaskList
import not.working.code.ican.repository.server.Repository
import not.working.code.ican.repository.server.RequestBuilder
import not.working.code.ican.utils.enums.TaskStatusEnum

class TaskListWorkerPresenter : MvpPresenter<TaskListWorkerView>() {

    private var filterToggle = false
    private var loadMyTasks = true

    fun changeFilterSelect(view: CardView) {
        viewState.changeSelectionFilter(view)
        loadMyTasks = !loadMyTasks
    }

    fun toggleFilter() {
        if (filterToggle) {
            viewState.filterHide()
        } else {
            viewState.filterShow()
        }
        filterToggle = !filterToggle
    }

    fun loadMyTasks(context: Context) {
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("GetMyTasks")
                .addParam("token", token)
                .build()
        Repository.getInstance().sendRequest(request,
                {response ->  if (response.code == 200) {
                    val arr = response.body!!.getJSONArray("tasks")
                    val tasks = ArrayList<TaskList>()
                    for (i in 0 until arr.length()) {
                        val item = arr.getJSONObject(i)
                        val executor = if(item.isNull("task_executor")) "Свободно"
                        else item.getString("task_executor")
                        tasks.add(
                                TaskList(
                                        id = item.getInt("task_id"),
                                        title = item.getString("task_title"),
                                        deadline = item.getString("task_deadline"),
                                        dayToDeadline = item.getInt("task_day_before_deadline"),
                                        executor = executor,
                                        status = TaskStatusEnum.values().first { it.int == item.getInt("task_status") }
                                ))
                    }
                    viewState.setAdapterList(tasks)
                } else {
                    viewState.showError(response.body!!.getString("user_message"))
                    viewState.setAdapterList(ArrayList<TaskList>())
                }},
                {errorMessage ->
                    viewState.showError(errorMessage)
                    viewState.setAdapterList(ArrayList<TaskList>())
                }
        )
    }

    fun loadFreeTasks(context: Context) {
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("GetFreeTasks")
                .addParam("token", token)
                .build()
        Repository.getInstance().sendRequest(request,
                {response ->  if (response.code == 200) {
                    val arr = response.body!!.getJSONArray("tasks")
                    val tasks = ArrayList<TaskList>()
                    for (i in 0 until arr.length()) {
                        val item = arr.getJSONObject(i)
                        val executor = if(item.isNull("task_executor")) "Свободно"
                        else item.getString("task_executor")
                        tasks.add(
                                TaskList(
                                        id = item.getInt("task_id"),
                                        title = item.getString("task_title"),
                                        deadline = item.getString("task_deadline"),
                                        dayToDeadline = item.getInt("task_day_before_deadline"),
                                        executor = executor,
                                        status = TaskStatusEnum.values().first { it.int == item.getInt("task_status") }
                                ))
                    }
                    viewState.setAdapterList(tasks)
                } else {
                    viewState.showError(response.body!!.getString("user_message"))
                    viewState.setAdapterList(ArrayList<TaskList>())
                }},
                {errorMessage ->
                    viewState.showError(errorMessage)
                    viewState.setAdapterList(ArrayList<TaskList>())
                }
        )
    }
}