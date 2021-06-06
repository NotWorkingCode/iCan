package not.working.code.ican.app.views

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType
import not.working.code.ican.data.Department
import not.working.code.ican.data.LowUser

@StateStrategyType(AddToEndSingleStrategy::class)
interface SettingsHeadView : MvpView{
    fun startLoad()
    fun endLoad()
    fun showAllDepartment(departments: ArrayList<Department>)
    fun showAllUser(users: ArrayList<LowUser>)
    fun showCreateUserDialog()
    fun showMessage(message: String)
    fun showActionUser()
    fun showActionDepartment()
}