#!/bin/bash

HEX_EXE_PATH='/home/burkhart/hex/exe/hex8.0.0-nogui.x64'
HEX_MAC_FILE='test1.mac'

# may also want -noexec here... not sure, see manual sec. 6.6.5
$HEX_EXE_PATH -ncpu 4 <$HEX_MAC_FILE >log0.txt
