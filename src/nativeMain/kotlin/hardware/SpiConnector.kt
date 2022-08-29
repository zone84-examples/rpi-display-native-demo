package hardware

import HardwareException
import bcm2835.*
import kotlinx.cinterop.toCValues
import utils.TinyLogger

class SpiConnector(val device: bcm2835SPIChipSelect, val logger: TinyLogger) {
    fun use(action: (SpiOperations) -> Unit) {
        init()
        try {
            action(DefaultSpiOperations())
        } finally {
            done()
        }
    }

    private fun init() {
        if (bcm2835_init() != 1) {
            throw HardwareException("Unable to initialize BCM2835. Are you running as root?")
        }
        if (bcm2835_spi_begin() != 1) {
            throw HardwareException("Unable to initialize SPI in BCM2835.")
        }

        bcm2835_spi_setChipSelectPolarity(device.toUByte(), 0)
        bcm2835_spi_set_speed_hz(20000000)
        bcm2835_spi_setDataMode(BCM2835_SPI_MODE2.toUByte())
        bcm2835_spi_chipSelect(device.toUByte())
        logger.info {  "BCM2835 SPI initialized..." }
    }

    private fun done() {
        bcm2835_spi_end()
        logger.info { "BCM2835 SPI finished" }
    }

    inner class DefaultSpiOperations : SpiOperations {
        private val buffer = ByteArray(1)

        override fun writeByte(byte: Byte) {
            buffer[0] = byte
            bcm2835_spi_writenb_hacked(buffer.toCValues(), 1)
        }

        override fun writeByte(byte: UByte) {
            buffer[0] = byte.toByte()
            bcm2835_spi_writenb_hacked(buffer.toCValues(), 1)
        }

        override fun writeBuffer(buffer: ByteArray) {
            bcm2835_spi_writenb_hacked(buffer.toCValues(), buffer.size.toUInt())
        }
    }
}

interface SpiOperations {
    fun writeByte(byte: Byte)
    fun writeByte(byte: UByte)
    fun writeBuffer(buffer: ByteArray)
}