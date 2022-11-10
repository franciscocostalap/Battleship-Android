package com.example.battleshipmobile.service

import com.example.battleshipmobile.utils.Problem


sealed class ServiceResult<T>{
    companion object{
        fun <T> success(value: T): ValueResult<T> = ValueResult(value)
        fun <T> error(problem: Problem): ProblemResult<T> = ProblemResult(problem)
    }
}

data class ValueResult<T> (val value: T): ServiceResult<T>()
data class ProblemResult<T>(val problem: Problem): ServiceResult<T>()
