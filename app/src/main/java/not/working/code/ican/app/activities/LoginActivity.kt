package not.working.code.ican.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import not.working.code.ican.R
import not.working.code.ican.app.presenters.LoginPresenter
import not.working.code.ican.app.views.LoginView

class LoginActivity : MvpAppCompatActivity(), LoginView {

    @InjectPresenter
    lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        super.onCreate(savedInstanceState)

        login_start.setOnClickListener {
            val login = login_login.text.toString()
            val pass = login_pass.text.toString()
            presenter.login(login, pass, applicationContext)
        }
        login_go_reg.setOnClickListener {
            startActivity(Intent(this, RegActivity::class.java))
            finish()
        }

    }
    override fun startLogin() {
        login_progress.visibility = View.VISIBLE
        login_start.isClickable = false
    }

    override fun showError(message: String) {
        login_progress.visibility = View.GONE
        login_start.isClickable = true
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun go(target: MvpAppCompatActivity) {
        login_progress.visibility = View.GONE
        startActivity(Intent(this, target::class.java))
        finish()
    }

}