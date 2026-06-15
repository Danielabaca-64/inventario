#!/usr/bin/env bash
set -e
if [ ! -f pom.xml ]; then
  echo "pom.xml no encontrado. Ejecuta desde la carpeta del proyecto."
  exit 1
fi
mvn -q -DskipTests package
echo "Compilación completa. Jar en target/inventario-1.0.0.jar"
