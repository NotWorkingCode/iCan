package not.working.code.ican.data

import not.working.code.ican.utils.enums.TaskStatusEnum
import java.util.*

data class Task(
    val title: String,
    val body: String,
    val dayToDeadline: Int,
    val executor: String,
    val creator: String,
    val status: TaskStatusEnum
)