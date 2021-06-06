package not.working.code.ican.app.presenters

import android.content.Context
import moxy.MvpPresenter
import not.working.code.ican.app.activities.LoginActivity
import not.working.code.ican.app.activities.TaskListHeadActivity
import not.working.code.ican.app.activities.TaskListWorkerActivity
import not.working.code.ican.app.views.SplashView
import not.working.code.ican.repository.server.Repository
import not.working.code.ican.repository.server.RequestBuilder

class SplashPresenter : MvpPresenter<SplashView>() {

    override fun onFirstViewAttach() {
        viewState.checkUserToken()
    }

    fun checkUser(context: Context) {
        val token = context.getSharedPreferences("storage", Context.MODE_PRIVATE).getString("token", "")
        val request = RequestBuilder("CheckUser")
                .addParam("token", token)
                .build()
        Repository.getInstance().sendRequest(request,
                {response -> if (response.code == 200) {
                    val id = response.body!!.getInt("uID")
                    val name = response.body.getString("name")
                    val role = response.body.getInt("role")

                    if (!response.body.isNull("department")) {
                        context.getSharedPreferences("storage", Context.MODE_PRIVATE).edit()
                                .putInt("department", response.body.getInt("department"))
                                .apply()
                    }

                    context.getSharedPreferences("storage", Context.MODE_PRIVATE).edit()
                            .putInt("id", id)
                            .putInt("role", role)
                            .putString("name", name)
                            .apply()

                    when(role) {
                        2 -> viewState.go(TaskListHeadActivity())
                        else -> viewState.go(TaskListWorkerActivity())
                    }
                }
                else {
                    viewState.go(LoginActivity())
                    context.getSharedPreferences("storage", Context.MODE_PRIVATE).edit().remove("token").apply()
                }},
                {errorMessage -> viewState.showError(errorMessage)}
        )
    }
}