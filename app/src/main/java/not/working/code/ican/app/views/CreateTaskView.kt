package not.working.code.ican.app.views

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import not.working.code.ican.data.Department
import not.working.code.ican.data.LowUser

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreateTaskView : MvpView {
    fun startLoad()
    fun endLoad()
    fun success()
    fun showError(message: String)
    fun setDepartments(departments: ArrayList<Department>)
    fun setUsers(users: ArrayList<LowUser>)
}