export CLASSPATH=".:./ojdbc8.jar"
echo $CLASSPATH
rm *.class
javac *.java

java MainProgram