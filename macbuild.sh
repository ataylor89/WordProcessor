if [ ! -f icons.icns ]; then
    ICON="icons/bookspenink.png"   
    mkdir icons.iconset
    sips -z 512 512   $ICON --out icons.iconset/icon_512x512.png
    cp $ICON icons.iconset/icon_512x512@2x.png
    sips -z 512 512   $ICON --out icons.iconset/icon_256x256@2x.png
    sips -z 256 256   $ICON --out icons.iconset/icon_256x256.png
    sips -z 256 256   $ICON --out icons.iconset/icon_128x128@2x.png
    sips -z 128 128   $ICON --out icons.iconset/icon_128x128.png
    sips -z 64 64     $ICON --out icons.iconset/icon_32x32@2x.png
    sips -z 32 32     $ICON --out icons.iconset/icon_32x32.png
    sips -z 32 32     $ICON --out icons.iconset/icon_16x16@2x.png
    sips -z 16 16     $ICON --out icons.iconset/icon_16x16.png
    iconutil -c icns icons.iconset
fi
mvn clean install
mvn assembly:single
jpackage --input target/ \
  --name WordProcessor \
  --main-jar WordProcessor-jar-with-dependencies.jar \
  --main-class wordprocessor.WordProcessor \
  --type dmg \
  --icon "icons.icns" \
  --app-version "1.0.0" \
  --vendor "Andrew's software" \
  --copyright "Copyright 2021" \
  --mac-package-name "Word Processor" \
  --verbose \
  --java-options '--enable-preview'
