import bcm2835.BCM2835_SPI_CS0
import bcm2835.RPI_BPLUS_GPIO_J8_16
import bcm2835.RPI_BPLUS_GPIO_J8_18
import hardware.*
import platform.posix.sleep
import utils.LogLevel
import utils.TinyLogger

private val logo = ubyteArrayOf(
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0xffU, 0xffU, 0xffU, 0xffU,
    0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU,
    0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x06U, 0x0eU, 0x0eU, 0x0eU, 0x0eU, 0x8eU, 0xceU, 0xeeU, 0xfeU, 0x7eU, 0x3eU, 0x1eU, 0x0eU, 0x00U, 0xfcU, 0xfeU,
    0xfeU, 0x0eU, 0x0eU, 0x0eU, 0x0eU, 0x0eU, 0x0eU, 0x0eU, 0xfeU, 0xfcU, 0x00U, 0x00U, 0xfeU, 0xfeU, 0xfeU, 0x3cU,
    0xf8U, 0xf0U, 0xc0U, 0x80U, 0x00U, 0x00U, 0xfeU, 0xfeU, 0x00U, 0x00U, 0xfeU, 0xfeU, 0xfeU, 0xceU, 0xceU, 0xceU,
    0xceU, 0xceU, 0xceU, 0xceU, 0xceU, 0x0eU, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0xffU, 0xffU, 0xffU, 0x83U,
    0x01U, 0x01U, 0x31U, 0x31U, 0x31U, 0x31U, 0x31U, 0x31U, 0x31U, 0x01U, 0x01U, 0xc7U, 0xffU, 0xffU, 0x7fU, 0x3fU,
    0x1fU, 0x0fU, 0x87U, 0xc3U, 0x01U, 0x01U, 0x01U, 0xffU, 0xffU, 0xffU, 0xffU, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0xc0U, 0xc0U, 0xc0U, 0xc0U, 0xc0U, 0xc0U, 0x00U, 0x80U, 0xc0U, 0xc0U, 0xc0U, 0xc0U, 0xc0U, 0xc0U, 0x00U, 0x80U,
    0xc0U, 0xc0U, 0xc0U, 0xc0U, 0xc0U, 0x80U, 0x00U, 0xc0U, 0xc0U, 0x00U, 0x00U, 0x00U, 0x00U, 0xc0U, 0xc0U, 0x00U,
    0x30U, 0x78U, 0x7cU, 0x7eU, 0x7fU, 0x77U, 0x73U, 0x71U, 0x70U, 0x70U, 0x70U, 0x70U, 0x30U, 0x00U, 0x3fU, 0x3fU,
    0x7fU, 0x70U, 0x70U, 0x70U, 0x70U, 0x70U, 0x70U, 0x78U, 0x7fU, 0x3fU, 0x00U, 0x00U, 0x7fU, 0x7fU, 0x7fU, 0x00U,
    0x00U, 0x01U, 0x03U, 0x0fU, 0x1fU, 0x3eU, 0x7fU, 0x7fU, 0x00U, 0x00U, 0x7fU, 0x7fU, 0x7fU, 0x71U, 0x71U, 0x71U,
    0x71U, 0x71U, 0x71U, 0x71U, 0x71U, 0x70U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0xffU, 0xffU, 0xffU, 0xe1U,
    0xc0U, 0x80U, 0x8eU, 0x8eU, 0x8eU, 0x8eU, 0x8eU, 0x8eU, 0x8eU, 0x80U, 0xc0U, 0xe1U, 0xf1U, 0xf0U, 0xf0U, 0xf0U,
    0xf0U, 0xf1U, 0xf1U, 0xf1U, 0xc0U, 0x80U, 0x80U, 0xf1U, 0xf1U, 0xffU, 0xffU, 0x00U, 0x00U, 0x60U, 0x60U, 0x00U,
    0x00U, 0x00U, 0x7fU, 0x7fU, 0x00U, 0x00U, 0x00U, 0x3fU, 0x7fU, 0x64U, 0x64U, 0x64U, 0x64U, 0x64U, 0x00U, 0x3fU,
    0x7fU, 0x60U, 0x60U, 0x60U, 0x71U, 0x31U, 0x00U, 0x7fU, 0x7fU, 0x06U, 0x06U, 0x06U, 0x06U, 0x7fU, 0x7fU, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0xffU, 0xffU, 0xffU, 0xffU,
    0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU,
    0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0xffU, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U,
    0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U, 0x00U
).toByteArray()

fun main() {
    val logger = TinyLogger(LogLevel.DEBUG)
    val spiConnector = SpiConnector(BCM2835_SPI_CS0, logger)
    spiConnector.use {spi ->
        val hal = Ssd1306Hal.create(
            spi = spi,
            logger = logger,
            dcPin = RPI_BPLUS_GPIO_J8_16,
            resetPin = RPI_BPLUS_GPIO_J8_18
        )
        hal.activateMode(ScreenMode(Orientation.NORTH, DrawMode.GRAPHICS_MODE))
        sleep(1)
        hal.drawFullScreenImage(logo)
        sleep(3)
        hal.activateMode(ScreenMode(Orientation.NORTH, DrawMode.TEXT_MODE))
        hal.clear()
        hal.write("hi zone84.tech\n")
        hal.write("this is test\n")
        hal.write("0123456789\n")
        hal.write("line 4\n")
        hal.write("line 5\n")
        hal.write("line 6\n")
        hal.write("line 7\n")
        hal.write("line 8\n")
        hal.transfer()
        sleep(10)
    }
}
