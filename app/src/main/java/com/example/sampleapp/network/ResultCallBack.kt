package com.example.sampleapp.network

interface ResultCallBack<T> {
    fun <T> onSuccess(response:T)
    fun onError(code:Int,errorMessage:String)
}