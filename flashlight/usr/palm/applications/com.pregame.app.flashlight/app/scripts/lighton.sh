#!/bin/sh

echo -n 1 >/sys/class/i2c-adapter/i2c-2/2-0033/avin
echo -n 100mA >/sys/class/i2c-adapter/i2c-2/2-0033/torch_current
echo -n torch >/sys/class/i2c-adapter/i2c-2/2-0033/mode


