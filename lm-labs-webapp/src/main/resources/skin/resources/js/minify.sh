ls *.js | while read fich
do
echo $fich
filename=${fich%.*}
echo $filename
java -jar ~/Workspaces/yuicompressor-2.4.7/build/yuicompressor-2.4.7.jar $fich > $filename.min.js
done
