package not.working.code.ican.app.views

import androidx.cardview.widget.CardView
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import not.working.code.ican.data.TaskList

@StateStrategyType(AddToEndSingleStrategy::class)
interface TaskListWorkerView : MvpView {
    fun filterShow()
    fun filterHide()
    fun changeSelectionFilter(id: CardView)
    fun setAdapterList(tasks: ArrayList<TaskList>)
    fun showError(message: String)
}