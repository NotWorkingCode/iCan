package not.working.code.ican.app.activities

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_task.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import not.working.code.ican.R
import not.working.code.ican.app.presenters.TaskPresenter
import not.working.code.ican.app.views.TaskView
import not.working.code.ican.data.Task
import not.working.code.ican.utils.enums.TaskStatusEnum

class TaskActivity : MvpAppCompatActivity(), TaskView {

    @InjectPresenter
    lateinit var presenter: TaskPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        if (intent.extras == null) {
            finish()
        } else if (!intent.extras!!.containsKey("task_id")) {
            finish()
        }

        presenter.loadTask(intent.extras!!.getInt("task_id"), this)

        t_status_button.setOnClickListener {
            presenter.nextStep(intent.extras!!.getInt("task_id"), this)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val role = getSharedPreferences("storage", Context.MODE_PRIVATE).getInt("role", 0)
        when(role) {
            in 1..2 -> menuInflater.inflate(R.menu.task_head, menu)
            else -> menuInflater.inflate(R.menu.task_worker, menu)
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.worker_close -> finish()
            R.id.worker_delete -> {
                AlertDialog.Builder(this)
                        .setTitle("Вы действительно хотите удалить задачу?")
                        .setPositiveButton("Да") { dialogInterface: DialogInterface, i: Int ->
                            if (i == Dialog.BUTTON_POSITIVE) {
                                presenter.deleteTask(intent.extras!!.getInt("task_id"), this)
                                dialogInterface.dismiss()
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
        return super.onOptionsItemSelected(item)
    }

    override fun showError(message: String) {
        Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_SHORT).show()
        Log.e("TASK_LOG", message)
    }

    override fun setTask(task: Task) {
        t_title.text = task.title
        t_body.text = task.body
        t_creator.text = task.creator
        var executor = "Не назначен"
        if (task.executor != "null") executor = task.executor
        t_executor.text = executor
        if (getSharedPreferences("storage", Context.MODE_PRIVATE).getInt("role", 0) == 2) {
            if (task.status == TaskStatusEnum.END) t_status_button.text = "Подтвердить выполнение"
            else t_status_button.visibility = View.GONE
        } else {
            when(task.status) {
                TaskStatusEnum.CREATE -> t_status_button.text = "Взяться за работу"
                TaskStatusEnum.WORK -> t_status_button.text = "Завершить"
                else -> t_status_button.visibility = View.GONE
            }
        }
        if (task.status == TaskStatusEnum.ARCHIVE && task.status == TaskStatusEnum.ALL) {
            t_status_button.visibility = View.GONE
        }
        t_deadline.text = task.dayToDeadline.toString()
        when (task.dayToDeadline) {
            in 0..2 -> t_deadline.chipBackgroundColor = getColorStateList(R.color.chip_red)
            in 3..5 -> t_deadline.chipBackgroundColor = getColorStateList(R.color.chip_orange)
            else -> t_deadline.chipBackgroundColor = getColorStateList(R.color.chip_green)
        }
    }

    override fun updateTaskStatus(status: TaskStatusEnum) {
        if (getSharedPreferences("storage", Context.MODE_PRIVATE).getInt("role", 0) == 2) {
            if (status == TaskStatusEnum.ARCHIVE) t_status_button.visibility = View.GONE
        } else {
            when(status) {
                TaskStatusEnum.WORK -> {
                    t_status_button.text = "Завершить"
                    t_executor.text = getSharedPreferences("storage", Context.MODE_PRIVATE).getString("name", "Джон Доу")
                }
                else -> t_status_button.visibility = View.GONE
            }
        }
    }

    override fun startLoad() {
        t_progress.visibility = View.VISIBLE
        t_status_button.isClickable = false
        t_deadline.visibility = View.GONE
    }

    override fun endLoad() {
        t_progress.visibility = View.GONE
        t_status_button.isClickable = true
        t_deadline.visibility = View.VISIBLE
    }

    override fun deleteTask() {
        Toast.makeText(this, "Вы успешно удалили задачу", Toast.LENGTH_SHORT).show()
        finish()
    }
}