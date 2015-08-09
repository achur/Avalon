#!/bin/bash

echo "Building Javascript..."

java -jar compiler.jar \
  'src/main/javascript/**.js' \
  'src/main/webapp/static/lib/goog/**.js' \
  '!**_test.js' \
  --externs externs/*.js \
  --js_output_file src/main/webapp/static/app.js \
  --output_manifest src/main/webapp/static/app.MF \
  --generate_exports \
  --export_local_property_definitions \
  --angular_pass \
  --closure_entry_point=avalon.application.module

echo "Symlinking static directories for local development..."

linkjs() {
  # If the symlink is not already present.
  if [ ! -L $TARGET_STATIC_DIR ]
  then
    rm -rf $TARGET_STATIC_DIR
    ln -s $SRC_STATIC_DIR $TARGET_STATIC_DIR
  fi
}

# Link static files including templates and libraries for development.
SRC_STATIC_DIR=$(pwd)/src/main/webapp/static/
TARGET_STATIC_DIR=$(pwd)/target/avalon-1.0-SNAPSHOT/static
linkjs

# Link SRC files directly for javascript development using manifest.
SRC_STATIC_DIR=$(pwd)/src/
TARGET_STATIC_DIR=$(pwd)/target/avalon-1.0-SNAPSHOT/src
linkjs
