package not.working.code.ican.app.presenters

import android.content.Context
import android.util.Log
import moxy.MvpPresenter
import not.working.code.ican.app.views.CreateUserView
import not.working.code.ican.data.Department
import not.working.code.ican.repository.server.Repository
import not.working.code.ican.repository.server.RequestBuilder

class CreateUserPresenter : MvpPresenter<CreateUserView>() {

    private val departments = ArrayList<Department>()

    fun changeRole(newRole: Int) {
        when (newRole) {
            0 -> viewState.hideDepartments()
            1 -> viewState.showDepartments()
        }
    }

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
                    viewState.setUpDepartments(departments)
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

    fun createUser(context: Context, login: String, name: String) {
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("AddAdministration")
                .addParam("token", token)
                .addParam("login", login)
                .addParam("name", name)
                .build()
        Repository.getInstance().sendRequest(request,
                {response ->  if (response.code == 200) {
                    viewState.endLoad()
                    viewState.success(response.body!!.getString("code"), login)
                } else {
                    viewState.showError(response.body!!.getString("user_message"))
                    viewState.endLoad()
                }},
                {errorMessage ->
                    viewState.showError(errorMessage)
                    viewState.endLoad()
                }
        )
    }

    fun createUser(context: Context, login: String, name: String, department: Int) {
        viewState.startLoad()
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val dID = departments[department].id.toString()
        val request = RequestBuilder("AddWorker")
                .addParam("token", token)
                .addParam("login", login)
                .addParam("name", name)
                .addParam("department", dID)
                .build()
        Repository.getInstance().sendRequest(request,
                {response ->  if (response.code == 200) {
                    viewState.endLoad()
                    viewState.success(response.body!!.getString("code"), login)
                } else {
                    viewState.showError(response.body!!.getString("user_message"))
                    viewState.endLoad()
                }},
                {errorMessage ->
                    viewState.showError(errorMessage)
                    viewState.endLoad()
                }
        )
    }
}