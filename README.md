## Kotlin Native and Raspberry Pi - pt. 2

This is an example project for [Kotlin Native and Raspberry Pi pt. 2: SSD1306 Display](https://zone84.tech/programming/kotlin-native-and-raspberry-pi-pt-2-ssd1306-display/)
article published at [zone84.tech](https://zone84.tech). You can run it and fully explore the presented solution.

### Required hardware

 * Raspberry PI Model 3 or 4
 * LCD/OLED display with SSD1306 chipset (e.g. Waveshare 0.96" OLED)

### Installation instructions

 1. make sure you have [bcm2835](https://www.airspayce.com/mikem/bcm2835/) library installed on your Raspberry Pi
 2. copy `bcm2835.h` and `bcm2835.a` from your Raspberry Pi to `/src/nativeInteropn/cinterop` directory
 3. clone [kotlinx-datetime](https://github.com/Kotlin/kotlinx-datetime) library and add the following target to its `/core/build.gradle.kts` build script:

    ```
    target("linuxArm32Hfp")
    ```

 4. create a build of **kotlinx-datetime** library for Raspberry Pi and publish it to Maven Local repository:

    ```
    ./gradlew clean publishLinuxArm32HfpPublicationToMavenLocal
    ```
    
 5. build this project
 6. upload the generated executable to Raspberry Pi
 7. run it on Raspberry Pi with `sudo`:

    ```
    sudo ./rpi-display-native-demo.kexe
    ```

Author: Tomasz Jędrzejewski

License: MIT License
