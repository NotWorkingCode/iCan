package not.working.code.ican.app.presenters

import android.content.Context
import android.util.Log
import moxy.MvpPresenter
import not.working.code.ican.app.views.CreateTaskView
import not.working.code.ican.data.Department
import not.working.code.ican.data.LowUser
import not.working.code.ican.data.TaskList
import not.working.code.ican.repository.server.Repository
import not.working.code.ican.repository.server.RequestBuilder
import not.working.code.ican.utils.enums.TaskStatusEnum

class CreateTaskPresenter : MvpPresenter<CreateTaskView>() {

    private var date: String? = null
    private val departments = ArrayList<Department>()
    private val users = ArrayList<LowUser>()

    fun loadDepartments(context: Context) {
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("GetDepartments")
                .addParam("token", token)
                .build()
        Repository.getInstance().sendRequest(request,
                {response ->  if (response.code == 200) {
                    val arr = response.body!!.getJSONArray("departments")
                    val departments = ArrayList<Department>()
                    for (i in 0 until arr.length()) {
                        val item = arr.getJSONObject(i)
                        departments.add(Department(
                                id = item.getInt("department_id"),
                                name = item.getString("department_name")
                        ))
                    }
                    this.departments.clear()
                    this.departments.addAll(departments)
                    viewState.setDepartments(departments)
                    viewState.endLoad()

                } else {
                    viewState.showError(response.body!!.getString("user_message"))
                    Log.e("TRST", request.url.toString())
                    viewState.endLoad()
                }},
                {errorMessage ->
                    viewState.showError(errorMessage)
                    viewState.endLoad()
                }
        )
    }

    fun loadWorkers(context: Context, selectDepartment: Int) {
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val department = departments.get(selectDepartment).id
        val request = RequestBuilder("GetUsersByDepartment")
                .addParam("token", token)
                .addParam("department", department.toString())
                .build()
        Repository.getInstance().sendRequest(request,
                {response ->  if (response.code == 200) {
                    val arr = response.body!!.getJSONArray("users")
                    val users = ArrayList<LowUser>()
                    for (i in 0 until arr.length()) {
                        val item = arr.getJSONObject(i)
                        users.add(LowUser(
                                id = item.getInt("user_id"),
                                name = item.getString("user_name")
                        ))
                    }
                    this.users.clear()
                    this.users.addAll(users)
                    viewState.setUsers(users)
                    viewState.endLoad()

                } else {
                    viewState.showError(response.body!!.getString("user_message"))
                    viewState.setUsers(ArrayList())
                    viewState.endLoad()
                }},
                {errorMessage ->
                    viewState.showError(errorMessage)
                    viewState.endLoad()
                }
        )
    }

    fun setDate(newDate: String) {
        date = newDate
    }

    fun create(title: String, body: String, department: Int, user: Int, context: Context) {
        if (date == null) {
            viewState.showError("Выберете время сдачи.")
        }
        if(title.isNullOrEmpty() or body.isNullOrEmpty()) {
            viewState.showError("Заполните все поля.")
        }
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = if(user == 0) {
            RequestBuilder("AddFreeTask")
                    .addParam("token", token)
                    .addParam("title", title)
                    .addParam("body", body)
                    .addParam("deadline", date)
                    .addParam("department", departments.get(department).id.toString())
                    .build()
        } else {

            RequestBuilder("AddTask")
                    .addParam("token", token)
                    .addParam("title", title)
                    .addParam("body", body)
                    .addParam("deadline", date)
                    .addParam("department", departments.get(department).id.toString())
                    .addParam("executor", users.get(user-1).id.toString())
                    .build()
        }
        Repository.getInstance().sendRequest(request,
                {response ->  if (response.code == 200) {
                    viewState.success()
                } else {
                    viewState.showError(response.body!!.getString("user_message"))
                    Log.e("TEST", request.url.toString())
                    viewState.endLoad()
                }},
                {errorMessage ->
                    viewState.showError(errorMessage)
                    viewState.endLoad()
                }
        )
    }


}