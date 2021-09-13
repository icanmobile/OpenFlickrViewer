/*
 *  HIYA CONFIDENTIAL
 *  __________________
 *
 *  (c) 2017 Hiya, Inc.
 *  All Rights Reserved.
 *
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Hiya, Inc. The intellectual and technical
 *  concepts contained herein are proprietary to Hiya, Inc.
 *  may be covered by U.S. and foreign patents, and are
 *  protected by trade secret or copyright law.  Dissemination
 *  of or reproduction of this material is strictly forbidden
 *  unless prior written permission is obtained from Hiya, Inc.
 *
 */

package com.icanmobile.openflickrviewer.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import android.net.NetworkInfo
import android.os.Build

object NetworkUtil {
    @SuppressWarnings("deprecation")
    fun isInternetConnected(context: Context?): Boolean {
        context?.let {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
                activeNetwork?.isConnectedOrConnecting == true
            } else {
                val activeNetwork = cm.activeNetwork
                //Because of SecurityException it might throw we need to wrap call in try/catch
                //See https://issuetracker.google.com/issues/175055271
                val networkCapabilities = try {
                    cm.getNetworkCapabilities(activeNetwork)
                } catch (throwable: Throwable) {
                    null
                }
                networkCapabilities != null &&
                        networkCapabilities.hasCapability(NET_CAPABILITY_VALIDATED) &&
                        networkCapabilities.hasCapability(NET_CAPABILITY_INTERNET)
            }
        }
        return false
    }
}
