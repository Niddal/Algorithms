rm ./bin/*.class

echo "please wait"
./compile.sh
for num in {1..9}
do
    echo "**************** running map$num.txt ****************"

	for algorithm in {1..3}
    do
	   ./run.sh ../data/map$num.txt $algorithm
	done
done
