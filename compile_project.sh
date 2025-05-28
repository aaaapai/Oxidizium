rm lib.h

declare -a targets=("x86_64-unknown-linux-gnu"
                    "aarch64-unknown-linux-gnu"
                    "x86_64-pc-windows-msvc"
                    "aarch64-pc-windows-msvc"
                    "x86_64-apple-darwin"
                    "aarch64-apple-darwin")

declare -a filenames=("liboxidizium_linux_x86"
                      "liboxidizium_linux_arm64"
                      "oxidizium_windows_x86"
                      "oxidizium_windows_arm64"
                      "liboxidizium_mac_x86"
                      "liboxidizium_mac_arm64")

declare -a extensions=(".so"
                       ".so"
                       ".dll"
                       ".dll"
                       ".dylib"
                       ".dylib")

jextract_path=""
script_dir="$(dirname "$0")"
cache_file="$script_dir/.jextract_path_cache"

jdk_path=""
jdk_cache_file="$script_dir/.jdk_path_cache"

function validate_path() {
    if [ -x "$1" ]; then
        return 0
    else
        return 1
    fi
}

function jextract() {
    $jextract_path --include-dir / --output src/main/java --target-package com.github.tatercertified.rust --library oxidizium lib.h
    # sed -i 's/Linker.Option.critical(false)/Linker.Option.critical(true)/' src/main/java/com/github/tatercertified/rust/lib_h.java
    # sed -i 's/public static final MethodHandle HANDLE = Linker.nativeLinker().downcallHandle(ADDR, DESC);/public static final MethodHandle HANDLE = Linker.nativeLinker().downcallHandle(ADDR, DESC, Linker.Option.critical(true));/' src/main/java/com/github/tatercertified/rust/lib_h.java
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

# Begin Program
echo "Make sure Docker, Jextract, and JDK 22 are installed"

if [ -f "$cache_file" ]
    then
    cached_path=$(cat "$cache_file")
    if validate_path "$cached_path"
    then
        jextract_path="$cached_path"
    else
        echo "Cached jextract path is invalid."
    fi
fi

while [ -z "$jextract_path" ]
    do
    echo "Jextract Path:"
    read -r jextract_path
    # Validate the entered path
    if validate_path "$jextract_path"
    then
        echo "$jextract_path" > "$cache_file"
    else
        echo "Invalid jextract path. Please ensure the path is correct and try again."
        jextract_path=""
    fi
done

if [ -f "$jdk_cache_file" ]
    then
    cached_path=$(cat "$jdk_cache_file")
    if validate_path "$cached_path"
    then
        jdk_path="$cached_path"
    else
        echo "Cached JDK path is invalid."
    fi
fi

while [ -z "$jdk_path" ]
    do
    echo "JDK 22 Path:"
    read -r jdk_path
    # Validate the entered path
    if validate_path "$jdk_path"
    then
        echo "$jdk_path" > "$jdk_cache_file"
    else
        echo "Invalid JDK path. Please ensure the path is correct and try again."
        jdk_path=""
    fi
done

echo "Compile for Mod Release? (Y/N)"
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
  cargo build -r
  jextract
  move_binary 2
fi

env JAVA_HOME="$jdk_path" ./gradlew build --no-daemon
