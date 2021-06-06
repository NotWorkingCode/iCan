package not.working.code.ican.app.views

import androidx.cardview.widget.CardView
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import not.working.code.ican.data.TaskList
import not.working.code.ican.utils.enums.TaskStatusEnum

@StateStrategyType(AddToEndSingleStrategy::class)
interface TaskListHeadView : MvpView {
    fun filterShow()
    fun filterHide()
    fun changeSelectionFilter(id: CardView)
    fun onFilter(status: TaskStatusEnum)
    fun offFilter()
    fun setAdapterList(tasks: ArrayList<TaskList>)
    fun showError(message: String)
}