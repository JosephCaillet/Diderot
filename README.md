# Diderot
A tool to help designing web routes. This project is still under active development. Almost every features are implemented, apart the plugin loading system.

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ee2f35e72c6942d59cc8d000941790c0)](https://www.codacy.com/app/josephcaillet/Diderot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PCYoshi/Diderot&amp;utm_campaign=Badge_Grade)

## Screenshots

The main window with generated web documentation

![Main window with generated web documentation](https://raw.githubusercontent.com/JosephCaillet/Diderot/master/rsc/diderot.png)

![The main application window](https://raw.githubusercontent.com/JosephCaillet/Diderot/master/rsc/mainWindow.png)

The project settings dialog

![The project settings dialog](https://raw.githubusercontent.com/JosephCaillet/Diderot/master/rsc/projectSettingsdialog.png)

## Build instructions

### Compile
```bash
mkdik build
javac -cp src/ -d build/ src/Main.java
```

### Create Jar file
```bash
cd build
jar cfe ../Diderot.jar Main .
cd ..
```

### Execute Jar file
```bash
cd ..
java -jar Diderot.jar
```

### Generate javadoc
```bash
cd src
javadoc -d ../diderot_docs -subpackages . Main.java
```
