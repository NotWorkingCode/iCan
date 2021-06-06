package not.working.code.ican.app.presenters

import android.content.Context
import android.util.Log
import moxy.MvpPresenter
import not.working.code.ican.app.views.SettingsHeadView
import not.working.code.ican.data.Department
import not.working.code.ican.data.LowUser
import not.working.code.ican.repository.server.Repository
import not.working.code.ican.repository.server.RequestBuilder

class SettingsHeadPresenter : MvpPresenter<SettingsHeadView>() {
    private val departments = ArrayList<Department>()
    private val users = ArrayList<LowUser>()

    private var selectDepartment = -1
    private var selectUser = -1

    fun loadDepartments(context: Context) {
        selectDepartment = -1
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
                    viewState.showAllDepartment(departments)
                    viewState.endLoad()

                } else {
                    viewState.showMessage(response.body!!.getString("user_message"))
                    viewState.endLoad()
                }},
                {errorMessage ->
                    viewState.showMessage(errorMessage)
                    viewState.endLoad()
                }
        )
    }

    fun deleteUser(context: Context)
    {
        val myID = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getInt("id", -1)
        if (myID == users[selectUser].id) {
            viewState.showMessage("Вы не можете удалить свой аккаунт")
            return
        }
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("DeleteUser")
                .addParam("token", token)
                .addParam("userid", users[selectUser].id.toString())
                .build()
        Log.e("TEST", request.url.toString())
        Repository.getInstance().sendRequest(request,
                {response ->  if (response.code == 200) {
                    viewState.showMessage("Вы успешно удалили пользователя!")
                } else {
                    viewState.showMessage(response.body!!.getString("user_message"))
                    viewState.endLoad()
                }},
                {errorMessage ->
                    viewState.showMessage(errorMessage)
                    viewState.endLoad()
                }
        )
    }

    fun loadUser(context: Context) {
        selectUser = -1
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("GetAllUser")
                .addParam("token", token)
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
                    viewState.showAllUser(users)
                    viewState.endLoad()

                } else {
                    viewState.showMessage(response.body!!.getString("user_message"))
                    viewState.endLoad()
                }},
                {errorMessage ->
                    viewState.showMessage(errorMessage)
                    viewState.endLoad()
                }
        )
    }

    fun deleteDepartment(context: Context)
    {
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("DeleteDepartment")
                .addParam("token", token)
                .addParam("department", departments[selectDepartment].id.toString())
                .build()
        Log.e("TEST", request.url.toString())
        Repository.getInstance().sendRequest(request,
                {response ->  if (response.code == 200) {
                    viewState.showMessage("Вы успешно удалили отдел!")
                } else {
                    viewState.showMessage(response.body!!.getString("user_message"))
                    viewState.endLoad()
                }},
                {errorMessage ->
                    viewState.showMessage(errorMessage)
                    viewState.endLoad()
                }
        )
    }

    fun createDepartment(context: Context, text: String) {
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("AddDepartment")
                .addParam("token", token)
                .addParam("name", text)
                .build()
        Repository.getInstance().sendRequest(request,
                {response ->  if (response.code == 200) {
                    viewState.showMessage("Вы успешно создали отдел!")
                } else {
                    viewState.showMessage(response.body!!.getString("user_message"))
                    viewState.endLoad()
                }},
                {errorMessage ->
                    viewState.showMessage(errorMessage)
                    viewState.endLoad()
                }
        )
    }

    fun selectDepartment(dep: Int) {
        selectDepartment = dep
        viewState.showActionDepartment()
    }

    fun selectUser(u: Int) {
        selectUser = u
        viewState.showActionUser()
    }
}