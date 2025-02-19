# Game - MetropoliTrans

# MetropoliTrans

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
- `android`: Android mobile platform. Needs Android SDK.

## Run Game

- `core`: Execute the MetropoliTrans-lwjgl3 class main on IDE or terminal.

- `android`: Install the app withe follow command on terminal:

```bash
./gradlew android:installDebug
```

Then run the app with the follow command on terminal:

```bash
./gradlew android:run
```

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `android:lint`: performs Android project validation.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

## MetropoliTrans Game Build Documentation

### Overview
MetropoliTrans is a game project that utilizes the LWJGL framework. This documentation provides instructions on how to build the game executable for Windows using the provided PowerShell script.

## Prerequisites
Before running the build script, ensure you have the following installed on your system:
- PowerShell
- Java Development Kit (JDK)
- 7-Zip
- Packr tool

## Build Instructions
1. **Open PowerShell**: Navigate to the project directory where the `win-build.ps1` script is located.

2. **Run the Build Script**: Execute the following command in PowerShell:
   ```powershell
   .\win-build.ps1
   ```

3. **Output**: After the build process completes, the executable and all necessary files will be located in the `metropolitrans-build\out-win` directory.

## File Structure
- `win-build.ps1`: The PowerShell script that automates the build process.
- `files/`: Directory containing resource files for the game.
- `lwjgl3/build/lib/`: Directory containing the JAR file for the game.

## Notes
- Ensure that the `files/icons/icon-house.png` file is present for setting the executable icon.
- The output directory will also contain a ZIP file of the build, created using 7-Zip.

## License
This project is licensed under the MIT License. See the LICENSE file for more details.
