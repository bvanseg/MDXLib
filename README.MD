Arisux Minecraft Development Library X
=============

## Links ##
* [Source]
* [Issues]

## Prerequisites ##
* [Java 7+]

## Contributing ##
I have a general set of [rules] I follow for my projects.
Do read through it if you do plan on contributing.

## Clone ##
The following steps will ensure your project is cloned properly

1. `git clone git@github.com:Ri5ux/AMDXLib.git`
2. `cd AMDXLib`

## Development Environment ##
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for
Windows systems in place of any 'gradle' command.

If you are a contributor, it is important that your development environment is setup properly. After cloning, as shown
above, follow the given steps for your ide:

#### [IntelliJ]

1. `gradle setupDecompWorkspace --refresh-dependencies`
2. `gradle idea`

#### [Eclipse]

1. `gradle setupDecompWorkspace --refresh-dependencies`
2. `gradle eclipse`

## Updating your Clone ##
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for
Windows systems in place of any 'gradle' command.

The following steps will update your clone with the official repo.

* `git pull`
* `gradle --refresh-dependencies`

## Building
__Note:__ If you do not have [Gradle] installed then use ./gradlew for Unix systems or Git Bash and gradlew.bat for
Windows systems in place of any 'gradle' command.

We use [Gradle] for AMDXLib.

Before you can build this mod, you must first create gradle.properties in the repositories root directory and create
the following entry:
curseForgeApiKey=00000-00000-00000-00000-00000

If the above entry is not added, the build process will fail.

In order to build AMDXLib you simply need to run the `gradle build` command.
You can find the compiled JAR file in `./build/libs` labeled similarly to 'AMDXLib-x.x.xjar'.

[Source]: https://github.com/Ri5ux/AMDXLib
[Issues]: https://github.com/Ri5ux/AMDXLib/issues
[Java]: http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html
[IntelliJ]: https://www.jetbrains.com/idea/
[Eclipse]: https://www.eclipse.org/
[Gradle]: https://www.gradle.org/
