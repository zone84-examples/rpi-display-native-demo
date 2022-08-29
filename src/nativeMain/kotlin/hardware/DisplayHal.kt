package hardware

interface DisplayHal {
    val textLineLength: Int

    fun activateMode(mode: ScreenMode)
    fun drawFullScreenImage(buffer: ByteArray)
    fun write(text: String)
    fun transfer()
    fun clear()
}