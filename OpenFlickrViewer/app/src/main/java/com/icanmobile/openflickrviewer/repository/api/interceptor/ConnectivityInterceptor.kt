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

import android.content.Context
import com.icanmobile.openflickrviewer.util.NetworkUtil
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtil.isInternetConnected(context)) {
            throw IOException("No network connection")
        }

        return chain.proceed(chain.request().newBuilder().build())
    }
}