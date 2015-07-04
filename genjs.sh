#!/bin/bash

java -jar compiler.jar \
  'src/main/javascript/**.js' \
  '!**_test.js' \
  --externs externs/*.js \
  --js_output_file src/main/webapp/static/app.js \
  --generate_exports \
  --angular_pass \
  --closure_entry_point=avalon.application.module
