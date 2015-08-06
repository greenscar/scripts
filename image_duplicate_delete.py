import os
import sys
import re
dir="/mnt/memories/photographs"

for arg in sys.argv:
	args = re.search(r"--(\S+)=(\S+)$", arg)
	if args is not None:
		if re.search(r"^year$", args.group(1)):
			subdir = args.group(2)

if subdir is None:
	print "usage: python image_duplicate_delete.py --year=XXXX"
	sys.exit(1)

fulldirpath = os.path.join(dir,subdir)
listing = os.listdir(fulldirpath)
for afile in listing:
	datestring, extension = os.path.splitext(afile)
	datestringa = datestring + "a"
	afilea = datestring + "a" + extension	
	#print os.path.join(fulldirpath, afilea)
	if os.path.exists(os.path.join(fulldirpath, afilea)):
		afile_size = os.path.getsize(os.path.join(fulldirpath, afile))
		afilea_size = os.path.getsize(os.path.join(fulldirpath, afilea))
		if afile_size != afilea_size:
			print "rm " + os.path.join(dir, subdir, afilea)
	#print datestring + "." + extension + " == " + datestring + "a." + extension	
	
