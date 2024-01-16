package org.foi.air.api.request_handlers

import ResponseListener
import org.foi.air.api.network.ApiService
import org.foi.air.api.network.ApiService.enqueueWithApiResponse
import org.foi.air.core.models.EventsResponseBody
import org.foi.air.core.network.RequestHandler

class GetEventsForUserRequestHandler(private var userId : Int, private var page : Int): RequestHandler<EventsResponseBody> {
    override fun sendRequest(responseListener: ResponseListener<EventsResponseBody>) {
        ApiService.eventService.getEvents(userId, page)
            .enqueueWithApiResponse(
                onSuccess = { responseListener.onSuccessfulResponse(it.data) },
                onError = { responseListener.onErrorResponse(it.error) },
                onFailure = { responseListener.onApiConnectionFailure(it)}
            )
    }
}