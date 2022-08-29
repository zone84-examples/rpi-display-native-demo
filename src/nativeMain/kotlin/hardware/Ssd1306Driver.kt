package hardware

import bcm2835.*
import utils.millisleep

class Ssd1306Driver(
    private val spi: SpiOperations,
    private val dcPin: RPiGPIOPin,
    private val resetPin: RPiGPIOPin
) {
    private val staticConfigurationSequence = ubyteArrayOf(
        0xA6U, 0xA8U, 0x3FU, 0xD3U, 0x00U, 0xD5U, 0x80U, 0xD9U, 0xF1U, 0xDAU,
        0x12U, 0xDBU, 0x40U, 0x8DU, 0x14U, 0xA4U, 0xA6U
    )

    fun commandEntireDisplayOn(enabled: Boolean) {
        if (enabled) {
            writeCommand(0xAFU)
        } else {
            writeCommand(0xAEU)
        }
    }

    fun commandResetPageCounter() {
        writeCommand(0x00U)
        writeCommand(0x10U)
    }

    fun commandSetInitialStartLine() {
        writeCommand(0x40U)
    }

    fun commandSetAddressingMode(addressingMode: Ssd1306AddressingMode) {
        writeCommand(0x20U)
        writeCommand(addressingMode.offset)
    }

    fun commandSetContrast(contrast: UByte) {
        writeCommand(0x81U)
        writeCommand(contrast)
    }

    fun commandSetOrientation(orientation: Orientation) {
        if (orientation == Orientation.NORTH) {
            writeCommand(0xA0U)
            writeCommand(0xC0U)
        } else {
            writeCommand(0xA1U)
            writeCommand(0xC8U)
        }
    }

    fun commandSendStaticInitializationSequence() {
        for (command in staticConfigurationSequence) {
            writeCommand(command)
        }
    }

    fun reset() {
        bcm2835_gpio_fsel(resetPin.toUByte(), BCM2835_GPIO_FSEL_OUTP.toUByte())
        bcm2835_gpio_fsel(dcPin.toUByte(), BCM2835_GPIO_FSEL_OUTP.toUByte())

        bcm2835_gpio_set(resetPin.toUByte())
        millisleep(millis = 10)
        bcm2835_gpio_clr(resetPin.toUByte())
        millisleep(millis = 100)
        bcm2835_gpio_set(resetPin.toUByte())
    }

    fun writeCommand(command: UByte) {
        bcm2835_gpio_clr(dcPin.toUByte())
        spi.writeByte(command)
    }

    fun writeData(data: ByteArray) {
        bcm2835_gpio_set(dcPin.toUByte())
        spi.writeBuffer(data)
    }
}

enum class Ssd1306AddressingMode(val offset: UByte) {
    HORIZONTAL(0x00U),
    VERTICAL(0x01U),
    PAGE(0x02U)
}
