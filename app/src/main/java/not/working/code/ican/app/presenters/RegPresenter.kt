package not.working.code.ican.app.presenters

import android.content.Context
import moxy.MvpPresenter
import not.working.code.ican.app.activities.LoginActivity
import not.working.code.ican.app.activities.TaskListHeadActivity
import not.working.code.ican.app.activities.TaskListWorkerActivity
import not.working.code.ican.app.views.RegView
import not.working.code.ican.repository.server.Repository
import not.working.code.ican.repository.server.RequestBuilder

class RegPresenter : MvpPresenter<RegView>() {
    fun reg(login: String, pass: String, code: String, context: Context) {
        if (login.isNullOrEmpty() or pass.isNullOrEmpty() or code.isNullOrEmpty()) {
            viewState.showError("Запролните все поля")
            return
        }

        viewState.startReg()

        val request = RequestBuilder("RegistrationUser")
                .addParam("login", login)
                .addParam("pass", pass)
                .addParam("code", code)
                .build()

        Repository.getInstance().sendRequest(request,
                { response ->
                    if (response.code == 200) {
                        viewState.go(LoginActivity())
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