<p align="center"><img alt="Logo" src="https://i.postimg.cc/vHgyC8nG/logo.png" height="250" /> </p>

### Description
DevLights are the smart home LED Stripes for Developers.
This is one example application how the DevLights API can be used. The client side [Minecraft Forge](https://mcforge.readthedocs.io/en/1.16.x/) modification reacts to the environment in game and sets the lights colors accordingly.
### Prerequisites
You just need the *java development kit* (jdk) version 8. Preferably in your *path* variable if you do not want to [reconfigure gradle](https://stackoverflow.com/questions/18487406/how-do-i-tell-gradle-to-use-specific-jdk-version).

### Getting started

```shell
git clone https://github.com/ProjektDevLights/MinecraftMod.git
cd MinecraftMod
# depending on your editor
./gradlew genEclipseRuns | genIntellijRuns | genVscodeRuns (gradlew.exe on windows)
# Optionally, if you want to login with your personal account add
#   --password YOUR_PASSWORD --username YOUR_USERNAME 
# to the args in your editors run configuration or .vscode/launch.json for Vscode
./gradlew runClient #or run the runClient configuration in your editor to start
```

To build a jar file you can add to any Forge modded Minecraft instance run `./gradlew assembleRelease`. (You can find the file in `build/libs`)

### Contributing
Feel free to open an issue on GitHub or send pull requests, when encountering issues or wanting to add some features. If you have any questions email [peters@teckdigital.de](mailto:peters@teckdigital.de) or [slaar@teckdigital.de](mailto:slaar@teckdigital.de).
