package not.working.code.ican.app.views

import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndSingleStrategy::class)
interface SplashView : MvpView {
    fun go(target: MvpAppCompatActivity)
    fun checkUserToken()
    fun showError(message: String)
}