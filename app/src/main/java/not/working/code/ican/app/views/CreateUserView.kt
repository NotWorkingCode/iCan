package not.working.code.ican.app.views

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import not.working.code.ican.data.Department

@StateStrategyType(AddToEndSingleStrategy::class)
interface CreateUserView : MvpView {
    fun showError(message: String)
    fun showDepartments()
    fun hideDepartments()
    fun setUpDepartments(dep: ArrayList<Department>)
    fun startLoad()
    fun endLoad()
    fun success(code: String, login: String)
}