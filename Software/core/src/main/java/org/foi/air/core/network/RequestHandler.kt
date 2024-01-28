package org.foi.air.core.network

import ResponseListener

interface RequestHandler<T> {
    fun sendRequest(responseListener: ResponseListener<T>)
}