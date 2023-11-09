package org.foi.air.core.network

interface RequestHandler {
    fun sendRequest(responseListener: ResponseListener)
}