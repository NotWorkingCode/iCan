package not.working.code.ican.app.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_task.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import not.working.code.ican.R
import not.working.code.ican.app.presenters.CreateTaskPresenter
import not.working.code.ican.app.views.CreateTaskView
import not.working.code.ican.data.Department
import not.working.code.ican.data.LowUser
import java.util.*
import kotlin.collections.ArrayList

class CreateTaskActivity : MvpAppCompatActivity(), CreateTaskView {

    @InjectPresenter
    lateinit var presenter: CreateTaskPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_task)
        presenter.loadDepartments(this)
        create_task_date.setOnClickListener {
            val calendar = GregorianCalendar.getInstance()
            val cyear = calendar.get(GregorianCalendar.YEAR)
            val cmonth = calendar.get(GregorianCalendar.MONTH)
            val cday = calendar.get(GregorianCalendar.DAY_OF_MONTH)
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                TimePickerDialog(this,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                            create_task_date.text = "${dayOfMonth.toString().padStart(2, '0')}.${(month + 1).toString().padStart(2, '0')}.$year ${hourOfDay.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
                            presenter.setDate("$year-${(month + 1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')} ${hourOfDay.toString().padStart(2, '0')}.${minute.toString().padStart(2, '0')}.00")
                        },
                        12,
                        0,
                        false).show()
            },
                    cyear,
                    cmonth,
                    cday).show()
        }
        create_task_create.setOnClickListener {
            val title = create_task_title.text.toString()
            val body = create_task_body.text.toString()
            val department = create_task_department.selectedItemPosition
            val create = create_task_executor.selectedItemPosition
            presenter.create(title, body, department, create, this)
        }
        create_task_department.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                presenter.loadWorkers(this@CreateTaskActivity, position)
            }

        }
    }

    override fun startLoad() {
        create_task_body.isActivated = false
        create_task_title.isActivated = false
        create_task_department.isActivated = false
        create_task_executor.isActivated = false
        create_task_date.isActivated = false
        create_task_load.visibility = View.VISIBLE
    }

    override fun endLoad() {
        create_task_body.isActivated = true
        create_task_title.isActivated = true
        create_task_department.isActivated = true
        create_task_executor.isActivated = true
        create_task_date.isActivated = true
        create_task_load.visibility = View.GONE
    }

    override fun success() {
        Toast.makeText(this, "Вы успешно создали задачу", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun setDepartments(departments: ArrayList<Department>) {
        val list = ArrayList<String>()
        departments.forEach {
            list.add(it.name)
        }
        create_task_department.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list)
    }

    override fun setUsers(users: ArrayList<LowUser>) {
        val list = ArrayList<String>()
        list.add("Любой")
        users.forEach {
            list.add(it.name)
        }
        create_task_executor.adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list)
    }
}