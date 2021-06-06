package not.working.code.ican.app.presenters

import android.content.Context
import androidx.cardview.widget.CardView
import moxy.MvpPresenter
import not.working.code.ican.R
import not.working.code.ican.app.views.TaskListHeadView
import not.working.code.ican.data.TaskList
import not.working.code.ican.repository.server.Repository
import not.working.code.ican.repository.server.RequestBuilder
import not.working.code.ican.utils.enums.TaskStatusEnum
import kotlin.collections.ArrayList

class TaskListHeadPresenter() : MvpPresenter<TaskListHeadView>() {

    private var filterToggle = false
    private var activeFilter = TaskStatusEnum.ALL

    fun toggleFilter() {
        if (filterToggle) {
            viewState.filterHide()
        } else {
            viewState.filterShow()
        }
        filterToggle = !filterToggle
    }

    fun changeFilterSelect(view: CardView) {
        viewState.changeSelectionFilter(view)
        when(view.id) {
            R.id.filter_all -> activeFilter = TaskStatusEnum.ALL
            R.id.filter_create -> activeFilter = TaskStatusEnum.CREATE
            R.id.filter_work -> activeFilter = TaskStatusEnum.WORK
            R.id.filter_end -> activeFilter = TaskStatusEnum.END
            R.id.filter_archive -> activeFilter = TaskStatusEnum.ARCHIVE
        }
        viewState.offFilter()
        if (activeFilter != TaskStatusEnum.ALL) viewState.onFilter(activeFilter)
    }

    fun loadTaskList(context: Context) {
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("GetAllTask")
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