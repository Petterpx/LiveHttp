package com.cloudx.livehttp

import androidx.lifecycle.ViewModel
import com.cloudx.core.error.launchHttp

/**
 * Created by Petterp
 * on 2020-01-19
 * Function:
 */
class CustomViewModel : ViewModel() {
    suspend fun test() {
        launchHttp {

        }
    }
}