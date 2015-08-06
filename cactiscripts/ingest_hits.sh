#!/bin/bash
logdir=/var/log/app
logfile=IngestService-requests.log
requestlog=$logdir/$logfile
outputfile=/tmp/ingest.out

more $requestlog | wc -l > $outputfile

exit 0
