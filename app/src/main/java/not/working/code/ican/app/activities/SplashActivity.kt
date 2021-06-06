package not.working.code.ican.app.activities

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import not.working.code.ican.R
import not.working.code.ican.app.presenters.SplashPresenter
import not.working.code.ican.app.views.SplashView

class SplashActivity : MvpAppCompatActivity(), SplashView {

    @InjectPresenter
    lateinit var presenter: SplashPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_Launcher)
        super.onCreate(savedInstanceState)
    }

    override fun go(target: MvpAppCompatActivity) {
        startActivity(Intent(this, target::class.java))
        overridePendingTransition(0, 0)
        finish()
    }

    override fun checkUserToken() {
        presenter.checkUser(applicationContext)
    }

    override fun showError(message: String) {
        Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_SHORT)
                .setAction("Повторить попытку") {
                    presenter.checkUser(applicationContext)
                }
                .show()
    }
}