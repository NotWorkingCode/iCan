package not.working.code.ican.app.presenters

import android.content.Context
import moxy.MvpPresenter
import not.working.code.ican.app.activities.TaskListHeadActivity
import not.working.code.ican.app.activities.TaskListWorkerActivity
import not.working.code.ican.app.views.LoginView
import not.working.code.ican.repository.server.Repository
import not.working.code.ican.repository.server.RequestBuilder

class LoginPresenter : MvpPresenter<LoginView>() {
    fun login(login: String, pass: String, context: Context) {
        if (login.isNullOrEmpty() or pass.isNullOrEmpty()) {
            viewState.showError("Запролните все поля")
            return
        }

        viewState.startLogin()

        val request = RequestBuilder("LoginUser")
                .addParam("login", login)
                .addParam("pass", pass)
                .build()

        Repository.getInstance().sendRequest(request,
                { response ->
                    if (response.code == 200) {
                        val id = response.body!!.getInt("ID")
                        val name = response.body.getString("name")
                        val role = response.body.getInt("role")
                        val token = response.body.getString("token")

                        if (!response.body.isNull("department")) {
                            context.getSharedPreferences("storage", Context.MODE_PRIVATE).edit()
                                    .putInt("department", response.body.getInt("department"))
                                    .apply()
                        }

                        context.getSharedPreferences("storage", Context.MODE_PRIVATE).edit()
                                .putInt("id", id)
                                .putInt("role", role)
                                .putString("name", name)
                                .putString("token", token)
                                .apply()

                        when(role) {
                            2 -> viewState.go(TaskListHeadActivity())
                            else -> viewState.go(TaskListWorkerActivity())
                        }
                    } else {
                        viewState.showError(response.body!!.getString("user_message"))
                    }
                },
                { errorMessage ->
                    viewState.showError(errorMessage)
                }
        )
    }
}