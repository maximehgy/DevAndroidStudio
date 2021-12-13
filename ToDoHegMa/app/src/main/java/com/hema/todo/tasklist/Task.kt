package com.hema.todo.tasklist

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Task(
    @SerialName("id")
    val id: String,
    @SerialName("title")
    var title: String = "Title",
    @SerialName("description")
    var description: String = "Description",
) : java.io.Serializable