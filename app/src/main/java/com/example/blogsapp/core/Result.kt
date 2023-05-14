package com.example.blogsapp.core

import java.lang.Exception

sealed class Result<out T> {
    class Loading<out T>: Result<T>()
    data class Success<out T>(val data: T): Result<T>()
    data class Faillure(val exception: Exception): Result<Nothing>()
}