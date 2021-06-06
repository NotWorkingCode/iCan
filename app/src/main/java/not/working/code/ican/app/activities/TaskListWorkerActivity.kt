package not.working.code.ican.app.activities

import android.animation.Animator
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_task_list_worker.*
import kotlinx.android.synthetic.main.include_filter.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import not.working.code.ican.R
import not.working.code.ican.app.adapters.TaskListAdapter
import not.working.code.ican.app.presenters.TaskListWorkerPresenter
import not.working.code.ican.app.views.TaskListWorkerView
import not.working.code.ican.data.TaskList


class TaskListWorkerActivity : MvpAppCompatActivity(), TaskListWorkerView {

    @InjectPresenter
    lateinit var presenter: TaskListWorkerPresenter

    private lateinit var adapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_task_list_worker)
        adapter = TaskListAdapter {
            startActivity(Intent(this, TaskActivity::class.java).putExtra("task_id", it))
        }
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f

        worker_task_recycler.layoutManager = LinearLayoutManager(this)
        worker_task_recycler.adapter = adapter

        presenter.loadMyTasks(this)

        arrayOf(filter_my, filter_free).forEach {view ->
            view.setOnClickListener {
                presenter.changeFilterSelect(view)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.filter_menu -> presenter.toggleFilter()
            R.id.filter_settings -> {
                val action = arrayOf("Выйти")
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Выберете действие: ")
                builder.setItems(action) { dialog, which ->
                    when(which) {
                        0 -> {
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
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun filterShow() {
        worker_filter.animate()
                .setDuration(1000)
                .translationY(worker_filter.height.toFloat())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        worker_filter.translationY = worker_filter.height.toFloat()
                    }
                })
                .start()
        worker_content.animate()
                .setDuration(1000)
                .translationY(worker_filter.height.toFloat())
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        worker_content.translationY = worker_filter.height.toFloat()
                    }
                })
                .start()
    }

    override fun filterHide() {
        worker_filter.animate()
                .setDuration(1000)
                .translationY(0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        worker_filter.translationY = 0f
                    }
                })
                .start()
        worker_content.animate()
                .setDuration(1000)
                .translationY(0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {}
                    override fun onAnimationCancel(animation: Animator?) {}
                    override fun onAnimationStart(animation: Animator?) {}
                    override fun onAnimationEnd(animation: Animator?) {
                        worker_content.translationY = 0f
                    }
                })
                .start()
    }

    override fun changeSelectionFilter(id: CardView) {
        arrayOf(filter_my, filter_free).forEach {
            it.setCardBackgroundColor(getColor(R.color.main))
        }
        id.setCardBackgroundColor(getColor(R.color.secondary))

        when (id.id) {
            R.id.filter_my -> presenter.loadMyTasks(this)
            R.id.filter_free -> presenter.loadFreeTasks(this)
        }
    }

    override fun setAdapterList(tasks: ArrayList<TaskList>) {
        adapter.changeTaskList(tasks)
    }

    override fun showError(message: String) {
        Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_SHORT).show()
    }
}