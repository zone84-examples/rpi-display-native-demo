package utils

import kotlinx.cinterop.cValue
import platform.posix.nanosleep
import platform.posix.timespec

fun millisleep(millis: Long) {
    val time = cValue<timespec> {
        tv_sec = millis.floorDiv(1000).toInt()
        tv_nsec = (millis % 1000 * 1000000).toInt()
    }
    nanosleep(time, null)
}
