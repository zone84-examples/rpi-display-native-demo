package hardware

import bcm2835.RPiGPIOPin
import hardware.Ssd1306AddressingMode.HORIZONTAL
import hardware.Ssd1306AddressingMode.VERTICAL
import utils.TinyLogger

@OptIn(ExperimentalUnsignedTypes::class)
class Ssd1306Hal(
    private val driver: Ssd1306Driver,
    private val logger: TinyLogger
) : DisplayHal {
    override val textLineLength: Int
        get() = 21

    private var columnCounter: Int = 0
    private var lineCounter: Int = 0
    private val font: FontResolver = FontResolver()
    private val framebuffer: UByteArray = UByteArray(1024)

    override fun activateMode(mode: ScreenMode) {
        logger.debug { "Resetting SSD1306 display" }
        driver.reset()
        logger.info { "Enabling ${mode.drawMode}"}
        driver.commandEntireDisplayOn(false)
        driver.commandResetPageCounter()
        driver.commandSetInitialStartLine()
        when (mode.drawMode) {
            DrawMode.TEXT_MODE -> driver.commandSetAddressingMode(VERTICAL)
            DrawMode.GRAPHICS_MODE -> driver.commandSetAddressingMode(HORIZONTAL)
        }
        driver.commandSetContrast(0xCFU)
        driver.commandSetOrientation(mode.orientation)
        driver.commandSendStaticInitializationSequence()
        driver.commandEntireDisplayOn(true)
    }

    override fun drawFullScreenImage(buffer: ByteArray) {
        driver.writeData(buffer)
    }

    fun drawByte(xLine: Int, yByte: Int, byte: UByte) {
        if (xLine > 127 || yByte > 7) {
            return
        }
        val location = xLine.shl(3) + yByte
        framebuffer[location] = byte
    }

    fun drawChar(x: Int, y: Int, char: Char) {
        if (x > 16 || y > 7) {
            return
        }
        var line = x * 6
        val symbol = font.pickSymbol(char)
        for (byte in symbol) {
            drawByte(line, y, byte)
            line++
        }
    }

    override fun write(text: String) {
        for (symbol in text) {
            if (columnCounter == textLineLength) {
                columnCounter = 0
                break
            }
            if (symbol == '\n') {
                fillLine()
                lineCounter++
                break
            } else {
                drawChar(columnCounter, lineCounter, symbol)
            }
            columnCounter++
        }
    }

    private fun fillLine() {
        for (j in columnCounter until textLineLength) {
            drawChar(j, lineCounter, ' ')
        }
        columnCounter = 0
    }

    override fun transfer() {
        driver.writeData(framebuffer.toByteArray())
        columnCounter = 0
        lineCounter = 0
    }

    override fun clear() {
        for (i in framebuffer.indices) {
            framebuffer[i] = 0x00U
        }
    }

    companion object {
        fun create(
            spi: SpiOperations,
            logger: TinyLogger,
            dcPin: RPiGPIOPin,
            resetPin: RPiGPIOPin
        ) = Ssd1306Hal(Ssd1306Driver(spi, dcPin, resetPin), logger)
    }
}