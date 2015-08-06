find . -maxdepth 1 -regex './[A-Za-z0-9\-\_]+' -type f -size 0 -exec rm {} \;
