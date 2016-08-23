Protocol Buffers - Google's data interchange format
Copyright 2008 Google Inc.
https://developers.google.com/protocol-buffers/

This package contains a precompiled binary version of the protocol buffer
compiler (protoc). This binary is intended for users who want to use Protocol
Buffers in languages other than C++ but do not want to compile protoc
themselves. To install, simply place this binary somewhere in your PATH.

Please refer to our official github site for more installation instructions:
  https://github.com/google/protobuf


用法：在本地文件夹下运行
./protoc --java_out=./ google/protobuf/xxx.prof 
讲在./目录下面生成一个 com.google...路径的java文件用来解析
