package not.working.code.ican.app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_reg.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import not.working.code.ican.R
import not.working.code.ican.app.presenters.RegPresenter
import not.working.code.ican.app.views.RegView

class RegActivity : MvpAppCompatActivity(), RegView {

    @InjectPresenter
    lateinit var presenter: RegPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_reg)
        super.onCreate(savedInstanceState)

        reg_start.setOnClickListener {
            val login = reg_login.text.toString()
            val pass = reg_pass.text.toString()
            val code = reg_code.text.toString()
            presenter.reg(login, pass,  code, this)
        }
        reg_go_login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
    override fun startReg() {
        reg_progress.visibility = View.VISIBLE
        reg_start.isClickable = false
    }

    override fun showError(message: String) {
        reg_progress.visibility = View.GONE
        reg_start.isClickable = true
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
    }

    override fun go(target: MvpAppCompatActivity) {
        reg_progress.visibility = View.GONE
        startActivity(Intent(this, target::class.java))
        finish()
    }

}