/*
 * HIYA CONFIDENTIAL
 * __________________
 *
 * (c) 2021 Hiya, Inc.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Hiya, Inc. The intellectual and technical
 * concepts contained herein are proprietary to Hiya, Inc.
 * may be covered by U.S. and foreign patents, and are
 * protected by trade secret or copyright law.  Dissemination
 * of or reproduction of this material is strictly forbidden
 * unless prior written permission is obtained from Hiya, Inc.
 */

package com.icanmobile.openflickrviewer.repository.api.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryOn429And503Interceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var response = chain.proceed(chain.request())

        if (response.code() == CODE_TOO_MANY_REQUESTS || response.code() == CODE_SERVICE_UNAVAILABLE) {
            val retryHeader = response.header(HEADER_RETRY_AFTER)

            try {
                Thread.sleep(getRetryAfterMillis(retryHeader))
            } catch (e: InterruptedException) {
                throw IOException(e)
            }

            response.close()
            response = chain.proceed(chain.request())
        }

        return response
    }

    private fun getRetryAfterMillis(retryHeader: String?): Long {
        return if (retryHeader.isNullOrEmpty()) DEFAULT_RETRY_AFTER_SECONDS * 1_000 else retryHeader.toLong() * 1_000
    }

    companion object {
        private const val CODE_TOO_MANY_REQUESTS = 429
        private const val CODE_SERVICE_UNAVAILABLE = 503
        private const val HEADER_RETRY_AFTER = "Retry-After"

        private const val DEFAULT_RETRY_AFTER_SECONDS = 5L
    }
}