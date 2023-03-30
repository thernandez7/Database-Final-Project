export CLASSPATH=".:./libraries/ojdbc8.jar:./libraries/jsoup-1.15.4.jar"
echo $CLASSPATH
rm *.class
javac *.java

java MainProgram
java Scraper