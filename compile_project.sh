rm lib.h

declare -a targets=("x86_64-unknown-linux-gnu"
                    "aarch64-unknown-linux-gnu"
                    "x86_64-pc-windows-msvc"
                    "aarch64-pc-windows-msvc"
                    "x86_64-apple-darwin"
                    "aarch64-apple-darwin")

declare -a filenames=("liboxidizium_linux_x64"
                      "liboxidizium_linux_arm64"
                      "oxidizium_windows_x64"
                      "oxidizium_windows_arm64"
                      "liboxidizium_mac_x64"
                      "liboxidizium_mac_arm64")

declare -a extensions=(".so"
                       ".so"
                       ".dll"
                       ".dll"
                       ".dylib"
                       ".dylib")

jextract_path=""

function jextract() {
    "\"${jextract_path}\"" --include-dir / --output src/main/java --target-package com.github.tatercertified.rust --library oxidizium lib.h
    sed -i 's/Linker.Option.critical(false)/Linker.Option.critical(true)/' src/main/java/com/github/tatercertified/rust/lib_h.java
}

function move_binary() {
  index="$1"

  if [ "$index" == 2 ] || [ "$index" == 3 ]
  then
    mv target/release/oxidizium"${extensions[index]}" src/main/resources/"${filenames[index]}""${extensions[index]}"
  else
    mv target/release/liboxidizium"${extensions[index]}" src/main/resources/"${filenames[index]}""${extensions[index]}"
  fi
}

echo "Make sure Docker and Jextract (Java 22) are installed"
echo "Jextract Path:"
read -r jextract_path
echo "Compile for Release? (Y/N)"
read -r input

if [ "$input" == "Y" ]
then
  cargo install cross --git https://github.com/cross-rs/cross
  for i in {0..5} ; do
    echo "Compiling ${filenames[i]}${extensions[i]} for ${targets[i]}"
    cross build -r --target "${targets[i]}"
    if [ "$i" == 0 ]
    then
      jextract
    fi
    move_binary i
  done
else
  cargo build
  jextract
  move_binary 2
fi

./gradlew build --no-daemon
