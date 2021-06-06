package not.working.code.ican.utils

import java.util.*

class DataTimeUtils {
    companion object {
        fun daysBetween(date1: Date, date2: Date): Int {
            return ((date2.time - date1.time) / (24 * 60 * 60 * 1000)).toInt()
        }
    }
}