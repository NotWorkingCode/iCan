package not.working.code.ican.data

import not.working.code.ican.utils.enums.TaskStatusEnum
import java.util.*

data class TaskList(
    val id: Int,
    val title: String,
    val deadline: String,
    val dayToDeadline: Int,
    val executor: String,
    val status: TaskStatusEnum
)