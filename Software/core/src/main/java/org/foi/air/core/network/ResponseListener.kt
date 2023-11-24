import org.foi.air.core.models.ErrorResponseBody

interface ResponseListener<T> {
    fun onSuccessfulResponse(response: T)
    fun onErrorResponse(response: ErrorResponseBody)
    fun onApiConnectionFailure(t: Throwable)
}
