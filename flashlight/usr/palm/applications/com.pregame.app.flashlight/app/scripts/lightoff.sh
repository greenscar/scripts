#!/bin/sh
echo -n shutdown >/sys/class/i2c-adapter/i2c-2/2-0033/mode
echo -n 0mA >/sys/class/i2c-adapter/i2c-2/2-0033/torch_current
echo -n 0 >/sys/class/i2c-adapter/i2c-2/2-0033/avin

