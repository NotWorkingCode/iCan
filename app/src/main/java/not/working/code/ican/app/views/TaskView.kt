package not.working.code.ican.app.views

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import not.working.code.ican.data.Task
import not.working.code.ican.utils.enums.TaskStatusEnum

@StateStrategyType(AddToEndSingleStrategy::class)
interface TaskView : MvpView {
    fun showError(message: String)
    fun setTask(task: Task)
    fun updateTaskStatus(status: TaskStatusEnum)
    fun startLoad()
    fun endLoad()
    fun deleteTask()
}
