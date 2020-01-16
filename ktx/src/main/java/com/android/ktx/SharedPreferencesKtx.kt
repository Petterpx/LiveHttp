package com.android.ktx

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Petterp
 * on 2020-01-16
 * Function:
 */

@SuppressLint("CommitPrefEdits")
fun putArg(context: Context, res: String) {
    val s = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit()
    s.putString("token", res)
}

@SuppressLint("CommitPrefEdits")
fun getArg(context: Context): String {
    val s = context.getSharedPreferences("data", Context.MODE_PRIVATE)
    return s.getString("token", "").toString()
}


@SuppressLint("CommitPrefEdits")
fun putArgSet(context: Context, set: HashSet<String>) {
    val s = context.getSharedPreferences("data", Context.MODE_PRIVATE).edit()
    s.putStringSet("set", set)
}


@SuppressLint("CommitPrefEdits")
fun getArgSet(context: Context): HashSet<String> {
    val s = context.getSharedPreferences("data", Context.MODE_PRIVATE)
    return s.getStringSet("set", HashSet<String>()) as HashSet<String>
}