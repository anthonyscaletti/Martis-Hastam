#!/bin/bash

g++ -g3 -ggdb -O0 -Wall -Wextra -Wno-unused -o AESencrypt AESencrypt.cpp -lcryptopp -static
g++ -g3 -ggdb -O0 -Wall -Wextra -Wno-unused -o AESdecrypt AESdecrypt.cpp -lcryptopp -static

