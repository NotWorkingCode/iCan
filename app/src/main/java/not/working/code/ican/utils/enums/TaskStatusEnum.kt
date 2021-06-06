package not.working.code.ican.utils.enums


enum class TaskStatusEnum(val int: Int?, val rus: String) {
    ALL(null, "Все"),
    CREATE(0, "Создана"),
    WORK(1, "В работе"),
    END(2, "Завершена"),
    ARCHIVE(3, "В архиве"),
}