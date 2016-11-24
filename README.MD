MDX Library (Minecraft Development Library X)
=============

## Links ##
* [Source]
* [Issues]

## Prerequisites ##
* [Java Development Kit 7 or Newer]
* [Gradle]

## Contributing ##
Please read the following if you plan on contributing to this project. It is very important to have your development 
environment set up properly. Aside from that, pull requests that do not follow the same format will not be accepted.

## 1) Clone The Repository ##
Open your command line, and execute the following commands:

1. `git clone git@github.com:Ri5ux/MDXLib.git`
2. `cd MDXLib`

## 2) Setup the Development Environment ##
__Note:__ If [Gradle] is not installed on your system, you may use ./gradlew on Unix based platforms or ./gradlew.bat
on Windows platforms.

__Note:__ If you experience any errors setting up your IDE, please run `gradle setupDecompWorkspace --refresh-dependencies`
to rule out any dependency related issues.

#### [IntelliJ IDEA]

1. `gradle setupDecompWorkspace`
2. `gradle idea`

#### [Eclipse]

1. `gradle setupDecompWorkspace`
2. `gradle eclipse`

## Updating your Repository ##

Run the following to sync your copy of the repository up with the latest changes:

* `git pull`

If you experience any new errors, it may be due to a dependency change. Please refresh your dependencies:
* `gradle --refresh-dependencies`

## Building the Project ##

Before you can initiate the build process, you must create a new text document with the name `gradle.properties` in the 
root directory of the repository. Then add `curseForgeApiKey=00000-00000-00000-00000-00000` to the start of the configuration
and save it.

__Note:__ If the above entry is not added, the build process will fail.

To initiate the build process, run `gradle build`.
You can find the compiled project artifact in `./build/libs`.

[Source]: https://github.com/Ri5ux/MDXLib
[Issues]: https://github.com/Ri5ux/MDXLib/issues
[Java]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[IntelliJ IDEA]: https://www.jetbrains.com/idea/
[Eclipse]: https://www.eclipse.org/
[Gradle]: https://www.gradle.org/
[Java Development Kit 7 or Newer]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
