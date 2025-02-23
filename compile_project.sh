rm lib.h

echo "Compile for Release? (Y/N)"
read input

if [ $input == "Y" ]
then 
   cargo build -r
else
   cargo build
fi
"D:\jextract-22\bin\jextract" --include-dir / --output src/main/java --target-package com.github.tatercertified.rust --library oxidizium lib.h

if [ $input == "Y" ]
then 
   mv target/release/oxidizium.dll src/main/resources
else
   mv target/debug/oxidizium.dll src/main/resources
fi

./gradlew build --no-daemon
