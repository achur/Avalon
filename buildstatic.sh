#!/bin/bash

echo "Building Javascript..."

java -jar compiler.jar \
  'src/main/javascript/**.js' \
  '!**_test.js' \
  --externs externs/*.js \
  --js_output_file src/main/webapp/static/app.js \
  --generate_exports \
  --angular_pass \
  --closure_entry_point=avalon.application.module

echo "Symlinking static directory for local development..."
SRC_STATIC_DIR=$(pwd)/src/main/webapp/static/
TARGET_STATIC_DIR=$(pwd)/target/avalon-1.0-SNAPSHOT/static
# If the symlink is not already present.
if [ ! -L $TARGET_STATIC_DIR ]
then
  rm -rf $TARGET_STATIC_DIR
  ln -s $SRC_STATIC_DIR $TARGET_STATIC_DIR
fi
