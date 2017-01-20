# Diderot
A tool to help designing web routes, with plugin system.

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/ee2f35e72c6942d59cc8d000941790c0)](https://www.codacy.com/app/josephcaillet/Diderot?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=PCYoshi/Diderot&amp;utm_campaign=Badge_Grade)

## Screenshots

The main window with generated web documentation
![Main window with generated web documentation](https://raw.githubusercontent.com/JosephCaillet/Diderot/master/rsc/diderot.png)

## Build instructions

### Compile
```bash
mkdir build
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
java -jar Diderot.jar
```

### Generate javadoc
```bash
cd src
javadoc -d ../diderot_docs -subpackages . Main.java -link http://docs.oracle.com/javase/8/docs/api/
```

If you want to see private stuff, add the `-private` command line parameter.

## Plugin
Diderot supports 3 types of plugins:

* Import plugin : Used to create routes/project from something (a source code for instance)
* Export plugin : Used to generate something (source code for a website, documentation, ...) from routes/project
* Edit plugin : Used to edit the current project (refactoring on route for example)

### Plugin installation

Get the `.jar` file of your plugin, and place it in the `plugins` directory. Il will be loaded on Diderot's startup.
Important: please note that Diderot does not control what a plugin does. Be sure to do not use a plugin that you do not trust.

### Create a plugin

Disclaimer: this is section covers only the basics of Diderot plugin development. If you want to know more, read the docs/sources.
(have a look on `model` and `plugin` packages)

Please note that this project is still in development (no release released yet), so you should expect changes in class interfaces. 

For each plugin type, there is an interface to implement, plus a base plugin interface that define general methods to get plugin's name, authors, ect.
As each plugin interfaces extends this base plugin interface, you do not have to explicitly implement it.
If you want to create a plugin with both import and export capabilities, feel free to do so, a plugin is not restricted to one type only.

Create a class for you plugin (We recommend the following package convention: internetDomainName.CompanyOrAuthor.pluginName)
and write your code.


In order to compile your plugin, you first need the `Diderot.jar` file, which contains plugin interfaces used by your plugin.
Important: you must not build a 'fat jar' containing `Diderot.jar` or any classes it contains, or it will causes problems.

Compile your plugin:
```bash
javac myPlugin.java -cp .:Diderot.jar
```

Create your .jar plugin:
```bash
jar cf myPlugin.jar myPlugin.class
```