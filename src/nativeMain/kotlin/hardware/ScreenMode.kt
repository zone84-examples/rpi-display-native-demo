package hardware

data class ScreenMode(
    val orientation: Orientation,
    val drawMode: DrawMode
)

enum class Orientation {
    NORTH,
    SOUTH
}

enum class DrawMode {
    TEXT_MODE,
    GRAPHICS_MODE
}
