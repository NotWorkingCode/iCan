package not.working.code.ican.app.activities

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_user.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import not.working.code.ican.R
import not.working.code.ican.app.presenters.CreateUserPresenter
import not.working.code.ican.app.views.CreateUserView
import not.working.code.ican.data.Department

class CreateUserActivity : MvpAppCompatActivity(), CreateUserView {

    @InjectPresenter
    lateinit var presenter: CreateUserPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        presenter.loadDepartments(this)

        create_user_role.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOf("Руководитель", "Исполнитель"))
        create_user_role.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.changeRole(position)
            }

        }
        create_user_create.setOnClickListener {
            val login = create_user_login.text.toString()
            val name = create_user_name.text.toString()
            val role = when (create_user_role.selectedItemPosition) {
                0 -> 2
                else -> 0
            }
            val department = create_user_department.selectedItemPosition
            if (role == 2) presenter.createUser(this, login, name)
            else presenter.createUser(this, login, name, department)
        }
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showDepartments() {
        create_user_department_hide.visibility = View.VISIBLE
    }

    override fun hideDepartments() {
        create_user_department_hide.visibility = View.GONE
    }

    override fun setUpDepartments(dep: ArrayList<Department>) {
        val list = ArrayList<String>()
        dep.forEach {
            list.add(it.name)
        }
        create_user_department.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list)
    }

    override fun startLoad() {
        create_user_login.isActivated = false
        create_user_role.isActivated = false
        create_user_department.isActivated = false
        create_user_create.isActivated = false
    }

    override fun endLoad() {
        create_user_login.isActivated = true
        create_user_role.isActivated = true
        create_user_department.isActivated = true
        create_user_create.isActivated = true
    }

    override fun success(code: String, login: String) {
        AlertDialog.Builder(this)
                .setTitle("Вы успешно создали пользователя!")
                .setMessage("Передайте пользователю следующие данные, для регистрации: \n\n\tЛогин: $login\n\tКод регистрации: $code\n\nВажно: Сохраните код регистрации! Без него невозможно зарегестрировать аккаунт!")
                .setCancelable(false)
                .setPositiveButton("Ок") { dialogInterface: DialogInterface, i: Int ->
                    finish()
                }
                .show()
    }
}