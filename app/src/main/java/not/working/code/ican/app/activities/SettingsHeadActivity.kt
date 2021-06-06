package not.working.code.ican.app.activities

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_settings.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import not.working.code.ican.R
import not.working.code.ican.app.presenters.SettingsHeadPresenter
import not.working.code.ican.app.views.SettingsHeadView
import not.working.code.ican.data.Department
import not.working.code.ican.data.LowUser


class SettingsHeadActivity : MvpAppCompatActivity(), SettingsHeadView {

    @InjectPresenter
    lateinit var presenter: SettingsHeadPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        settings_task_create.setOnClickListener {
            startActivity(Intent(this, CreateTaskActivity::class.java))
        }
        settings_department_create.setOnClickListener {
            showCreateUserDialog()
        }
        settings_department_list.setOnClickListener {
            presenter.loadDepartments(this)
        }
        settings_user_create.setOnClickListener {
            startActivity(Intent(this, CreateUserActivity::class.java))
        }
        settings_user_list.setOnClickListener {
            presenter.loadUser(this)
        }
        settings_exit.setOnClickListener {
            AlertDialog.Builder(this)
                    .setTitle("Вы действительно хотите выйти из аккаунта?")
                    .setPositiveButton("Да") { dialogInterface: DialogInterface, i: Int ->
                        if (i == Dialog.BUTTON_POSITIVE) {
                            getSharedPreferences("storage", Context.MODE_PRIVATE).edit().clear().apply()
                            startActivity(Intent(this, LoginActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK))
                            finish()
                            dialogInterface.dismiss()
                        }
                    }
                    .setNegativeButton("Нет") { dialogInterface: DialogInterface, i: Int ->
                        if (i == Dialog.BUTTON_NEGATIVE) {
                            dialogInterface.dismiss()
                        }
                    }
                    .show()
        }
    }

    override fun startLoad() {
        settings_department_create.isActivated = false
        settings_department_list.isActivated = false
        settings_task_create.isActivated = false
        settings_user_create.isActivated = false
        settings_user_list.isActivated = false
        settings_exit.isActivated = false
    }

    override fun endLoad() {
        settings_department_create.isActivated = true
        settings_department_list.isActivated = true
        settings_task_create.isActivated = true
        settings_user_create.isActivated = true
        settings_user_list.isActivated = true
        settings_exit.isActivated = true
    }

    override fun showAllDepartment(departments: ArrayList<Department>) {
        val dep = ArrayList<String>()
        departments.forEach {
            dep.add(it.name)
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберете отдел: ")
        builder.setItems(dep.toTypedArray()) { dialog, which ->
            presenter.selectDepartment(which)
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun showAllUser(users: ArrayList<LowUser>) {
        val u = ArrayList<String>()
        users.forEach {
            u.add(it.name)
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберете пользователя: ")
        builder.setItems(u.toTypedArray()) { dialog, which ->
            presenter.selectUser(which)
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun showCreateUserDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Введите название отдела: ")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { dialog, which -> presenter.createDepartment(this, input.text.toString()) }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }

        builder.show()
    }

    override fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun showActionUser() {
        val action = arrayOf("Удалить")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберете действие: ")
        builder.setItems(action) { dialog, which ->
            when(which) {
                0 -> {
                    AlertDialog.Builder(this)
                            .setTitle("Вы действительно хотите удалить пользователя?")
                            .setMessage("Важно: Удаление пользователя повлечет за собой удаление всех поставленных ему задач!")
                            .setPositiveButton("Да") { dialogInterface: DialogInterface, i: Int ->
                                if (i == Dialog.BUTTON_POSITIVE) {
                                    dialogInterface.dismiss()
                                    presenter.deleteUser(this)
                                }
                            }
                            .setNegativeButton("Нет") { dialogInterface: DialogInterface, i: Int ->
                                if (i == Dialog.BUTTON_NEGATIVE) {
                                    dialogInterface.dismiss()
                                }
                            }
                            .setCancelable(true)
                            .show()
                }
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun showActionDepartment() {
        val action = arrayOf("Удалить")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберете действие: ")
        builder.setItems(action) { dialog, which ->
            when(which) {
                0 -> {
                    AlertDialog.Builder(this)
                            .setTitle("Вы действительно хотите удалить отдел?")
                            .setMessage("Важно: Удаление отдела повлечет за собой удаление всех пользователей и задач, прикрепленных к этому отделу!")
                            .setPositiveButton("Да") { dialogInterface: DialogInterface, i: Int ->
                                if (i == Dialog.BUTTON_POSITIVE) {
                                    dialogInterface.dismiss()
                                    presenter.deleteDepartment(this)
                                }
                            }
                            .setNegativeButton("Нет") { dialogInterface: DialogInterface, i: Int ->
                                if (i == Dialog.BUTTON_NEGATIVE) {
                                    dialogInterface.dismiss()
                                }
                            }
                            .setCancelable(true)
                            .show()
                }
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}