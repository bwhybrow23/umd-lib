package io.vinicius.umd.util

import okio.FileSystem
import okio.NodeJsFileSystem

actual val fileSystem: FileSystem = NodeJsFileSystem