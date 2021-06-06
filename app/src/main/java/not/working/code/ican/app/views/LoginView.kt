package not.working.code.ican.app.views

import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface LoginView : MvpView {
    fun startLogin()
    fun showError(message: String)
    fun go(target: MvpAppCompatActivity)
}