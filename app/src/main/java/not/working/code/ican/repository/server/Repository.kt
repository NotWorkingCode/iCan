package not.working.code.ican.repository.server

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.lang.Exception

class Repository private constructor() {

    companion object {
        private var instance: Repository? = null
        fun getInstance(): Repository {
            if(instance == null) instance = Repository()
            return instance!!
        }
    }

    private val client = OkHttpClient()

    fun sendRequest(request: Request, success: (Response) -> Unit, error: (String) -> Unit) {
        Single.create<Response> {
            val r = client.newCall(request).execute()
            val json = JSONObject(r.body!!.string())
            r.close()
            val response = try {
                Response(
                        code = json.getInt("response_code"),
                        body = json.getJSONObject("body")
                )
            } catch (e: Exception) {
                Response(
                        code = json.getInt("response_code"),
                        body = null
                )
            }
            it.onSuccess(response)
        }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {r -> success(r)},
                {e -> error(e.message.toString())}
            )
    }
}