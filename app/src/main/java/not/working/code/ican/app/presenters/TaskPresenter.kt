package not.working.code.ican.app.presenters

import android.content.Context
import android.util.Log
import moxy.MvpPresenter
import not.working.code.ican.app.views.TaskView
import not.working.code.ican.data.Task
import not.working.code.ican.repository.server.Repository
import not.working.code.ican.repository.server.RequestBuilder
import not.working.code.ican.utils.enums.TaskStatusEnum

class TaskPresenter : MvpPresenter<TaskView>() {
    fun loadTask(taskID: Int, context: Context) {
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("GetTask")
                .addParam("token", token)
                .addParam("taskid", taskID.toString())
                .build()
        Log.e("TASL", request.url.toString())
        Repository.getInstance().sendRequest(request,
            {response ->
                if (response.code == 200) {
                    response.body?.apply {
                        val task = Task(
                                title = getString("task_title"),
                                body = getString("task_body"),
                                dayToDeadline = getInt("task_day_before_deadline"),
                                executor = getString("task_executor"),
                                creator = getString("task_creator"),
                                status = TaskStatusEnum.values().first { it.int == getInt("task_status") }
                        )
                        viewState.endLoad()
                        viewState.setTask(task)
                    }
                } else {
                    viewState.endLoad()
                    viewState.showError(response.body!!.getString("user_message"))
                }
            },
            { errorMessage ->
                viewState.showError(errorMessage)
                viewState.endLoad()
            }
        )
    }
    fun deleteTask(taskID: Int, context: Context) {
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("DeleteTask")
                .addParam("token", token)
                .addParam("taskid", taskID.toString())
                .build()
        Repository.getInstance().sendRequest(request,
                {response ->
                    if (response.code == 200) {
                        viewState.endLoad()
                        viewState.deleteTask()
                    } else {
                        viewState.endLoad()
                        viewState.showError(response.body!!.getString("user_message"))
                    }
                },
                { errorMessage ->
                    viewState.showError(errorMessage)
                    viewState.endLoad()
                }
        )
    }
    fun nextStep(taskID: Int, context: Context) {
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("TaskNextStep")
                .addParam("token", token)
                .addParam("taskid", taskID.toString())
                .build()
        Repository.getInstance().sendRequest(request,
                {response ->
                    if (response.code == 200) {
                        val status = TaskStatusEnum.values().first { it.int == response.body!!.getInt("task_status") }
                        viewState.updateTaskStatus(status)
                        viewState.endLoad()
                    } else {
                        viewState.endLoad()
                        viewState.showError(response.body!!.getString("user_message"))
                    }
                },
                { errorMessage ->
                    viewState.showError(errorMessage)
                    viewState.endLoad()
                }
        )
    }
}
