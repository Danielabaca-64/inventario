#!/usr/bin/env bash
# Apache Maven Wrapper (minimal)
set -e
scriptDir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
wrapperJar="$scriptDir/.mvn/wrapper/maven-wrapper.jar"
if [ ! -f "$wrapperJar" ]; then
  echo "Missing $wrapperJar"
  echo "You can run 'mvn -N io.takari:maven:wrapper' to generate the wrapper, or ask me to add it." >&2
  exit 1
fi
exec java -jar "$wrapperJar" "$@"
