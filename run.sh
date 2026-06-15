#!/usr/bin/env bash
set -e
if [ ! -f target/inventario-1.0.0.jar ]; then
  echo "Jar no encontrado. Ejecuta ./build.sh primero."
  exit 1
fi
java -jar target/inventario-1.0.0.jar
