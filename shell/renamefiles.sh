#!/bin/sh

# first, we fix the directories
if [ $# -ne 1 ]; then
   echo 1>&2 Usage: renamefiles.sh DIR
   exit 127
fi

FOLDER=$1
for name in $(find $FOLDER/ -type d -print)
do
echo $name
	if [ $name != "." ] ; then
		oldname="$(echo $name)"
		newname="$(echo $name | sed 's/ /_/g')"
		newname="$(echo $newname | sed 's/\[/\(/g')"
		newname="$(echo $newname | sed 's/\]/\)/g')"
		newname="$(echo $newname | sed 's/__/_/g')"
		if [ "$oldname" != "$newname" ] ; then
			echo "renaming \"$oldname\" to $newname"
			mv "$oldname" "$newname"
		fi
	fi
done

echo ""
echo "done with directories, fixing individual files...\n\n\n"

# now let's fix the files therein using almost identical code

for name in $(find $FOLDER/ -type f -print)
do
	oldname="$(echo $name)"
	newname="$(echo $name | sed 's/ /_/g')"
	newname="$(echo $newname | sed 's/\[/\(/g')"
	newname="$(echo $newname | sed 's/\]/\)/g')"
	newname="$(echo $newname | sed 's/__/_/g')"
	if [ "$oldname" != "$newname" ] ; then
		echo "renaming \"$oldname\" to $newname"
		mv "$oldname" "$newname"
	fi
done

exit 0


#for i in *.mp3;do NEWF=`echo $i | sed 's/ /_/g'` ; mv "$i" $NEWF ; done

