package com.monke.yandextodo.data.repository

data class RepositoryResponse(
    var statusCode: Int,
    var body: Any? = null,
    var message: String
)