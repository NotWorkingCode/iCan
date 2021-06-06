package not.working.code.ican.app.activities

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_task_list_head.*
import kotlinx.android.synthetic.main.include_head_filter.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import not.working.code.ican.R
import not.working.code.ican.app.adapters.TaskListAdapter
import not.working.code.ican.app.presenters.TaskListHeadPresenter
import not.working.code.ican.app.views.TaskListHeadView
import not.working.code.ican.data.TaskList
import not.working.code.ican.utils.enums.TaskStatusEnum

class TaskListHeadActivity : MvpAppCompatActivity(), TaskListHeadView {

    @InjectPresenter
    lateinit var presenter: TaskListHeadPresenter

    private lateinit var adapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_task_list_head)
        adapter = TaskListAdapter {
            startActivity(Intent(this, TaskActivity::class.java).putExtra("task_id", it))
        }
        super.onCreate(savedInstanceState)
        supportActionBar?.elevation = 0f

        head_task_recycler.layoutManager = LinearLayoutManager(this)
        head_task_recycler.adapter = adapter

        presenter.loadTaskList(this)

        arrayOf(filter_all, filter_create, filter_work, filter_end, filter_archive).forEach {view ->
            view.setOnClickListener {
                presenter.changeFilterSelect(view)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadTaskList(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.task_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.filter_menu -> presenter.toggleFilter()
            R.id.filter_settings -> startActivity(Intent(this, SettingsHeadActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun filterShow() {
        head_filter.animate()
            .setDuration(1000)
            .translationY(head_filter.height.toFloat())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    head_filter.translationY = head_filter.height.toFloat()
                }
            })
            .start()
        head_content.animate()
            .setDuration(1000)
            .translationY(head_filter.height.toFloat())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    head_filter.translationY = head_filter.height.toFloat()
                }
            })
            .start()
    }

    override fun filterHide() {
        head_filter.animate()
            .setDuration(1000)
            .translationY(0f)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    head_filter.translationY = 0f
                }
            })
            .start()
        head_content.animate()
            .setDuration(1000)
            .translationY(0f)
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationEnd(animation: Animator?) {
                    head_filter.translationY = 0f
                }
            })
            .start()
    }

    override fun changeSelectionFilter(view: CardView) {
        arrayOf(filter_all, filter_create, filter_work, filter_end, filter_archive).forEach {view ->
            view.setCardBackgroundColor(getColor(R.color.main))
        }
        view.setCardBackgroundColor(getColor(R.color.secondary))
    }

    override fun onFilter(status: TaskStatusEnum) {
        adapter.onFilter(status)
    }

    override fun offFilter() {
        adapter.offFilter()
    }

    override fun setAdapterList(tasks: ArrayList<TaskList>) {
        adapter.changeTaskList(tasks)
    }

    override fun showError(message: String) {
        Snackbar.make(window.decorView.rootView, message, Snackbar.LENGTH_SHORT).show()
    }
}