package not.working.code.ican.repository.server

import okhttp3.HttpUrl
import okhttp3.Request

class RequestBuilder(private val method: String) {

    private val url = HttpUrl.Builder()
        .scheme("http")
        .host("s92025i6.beget.tech")
        .addQueryParameter("method", method)

    fun addParam(param: String, value: String?): RequestBuilder {
        url.addQueryParameter(param, value)
        return this
    }

    fun build(): Request {
        return Request.Builder()
            .url(url.build())
            .header("User-Agent", "Mozilla/5.0 (Linux; Android 9; Redmi Note 8T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.91 Mobile Safari/537.36")
            .build()
    }
}