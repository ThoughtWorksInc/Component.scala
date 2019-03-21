lazy val Component = crossProject

lazy val ComponentJVM = Component.jvm

lazy val ComponentJS = Component.js

ThisBuild / organization := "com.thoughtworks.binding"

dynverSeparator in ThisBuild := "-"

skip in publish := true
