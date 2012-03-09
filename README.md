# Application development with the OSGi framework #
This repository represents the companion code for my Diploma Thesis Project available [here](http://dkum.uni-mb.si/IzpisGradiva.php?id=18508) (Slovenian Language). The purpose of this project is to research Java enterprise application development with the OSGi framework, analyze it's different usage/deployment scenarios and discover what the benefits of using this framework are by building a chat application, inspired by other popular chat applications like IRC/MSN, but providing a slick Web 2.0/Mobile user interface built with Adobe Flex. For more details about this project please see the full Diploma Thesis document linked above. Technologies used are: Eclipse Equinox, Eclipse Virgo, Apache Tomcat, BlazeDS, Spring, Swing, Adobe Flex, Parsley.

## Projects ##
A short description of every project follows. All of the projects are Eclipse projects and are easily imported into the Eclipse/STS/Flash Builder IDE.  

* **si.unimb.feri.osgichat** - OSGi module containing all the interfaces.
* **si.unimb.feri.osgichat.impl** - OSGi module containing the implementation of the chat service.
* **si.unimb.feri.osgichat.nickserv.impl** - OSGi module providing an in-memory implementation of the nickname service.
* **si.unimb.feri.osgichat.test** - OSGi module containing the tests.
* **si.unimb.feri.osgichat.hook** - OSGi module providing a hook to retrieve the chat service outside the OSGi container. Used when the OSGi framework is deployed inside a web application.
* **si.unimb.feri.osgichat.client** - OSGi module containing the Java Swing GUI used to test all of the functionality of the chat service.
* **si.unimb.feri.osgichat.webclient** - OSGi web module providing the BlazeDS remoting/messaging destinations for the Flex client.
* **webclient** - Web application containing and launching the entire OSGi framework when deploying to the Apache Tomcat application server.
* **osgichatcommon** - Flash Builder project containing the common code used by the desktop/web/mobile clients.
* **osgichatdesktop** - Flash Builder project containing the Adobe AIR desktop client.
* **osgichatweb** - Flash Builder project containing the web client.
* **osgichatmobile** - Flash Builder project containing the Adobe AIR mobile client (tested on Android).

## Building ##
Maven is used to build the projects. The easiest and quickest way to build the application is to build all of the projects and install them into the local Maven repository. The Flex client needs to be compiled manually with the Flex SDK (or with the help of the Flash Builder) and copied to the appropriate web application/module folder or packaged as an AIR application and installed on the desktop/mobile device (the projects were built and tested using a prerelease version of the Adobe Flex 4.5 SDK so there might be differences between the prerelease and the stable final release build of the Flex 4.5 SDK as well).