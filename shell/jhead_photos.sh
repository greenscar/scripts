path="/media/memories/photographs";
year=2007;
        dapath="$path/$year";
        echo $dapath;
        for i in 1 2 3 4 5 6 7 8 9 10 11 12;
        do
                if [ $i -lt 10 ];
                then
                #       echo "jhead -ft  $dapath/$year-0$i*.jpg";
			`jhead -nf%Y-%m-%d_-_%H%M%S $dapath/$year-0$i*.jpg`

                else
                #       echo "jhead -ft  $dapath/$year-$i*.jpg";
 			`jhead -nf%Y-%m-%d_-_%H%M%S $dapath/$year-$i*.jpg`
                fi;
        done;
