TARGET=main.o vehicle.o motor.o

projet: main.o vehicle.o motor.o
	g++ -o projet main.o vehicle.o motor.o

main.o: main.cpp   
	g++ -o main.o -c main.cpp
	
vehicle.o: vehicle.cpp
	g++ -o vehicle.o -c vehicle.cpp
	
motor.o: motor.cpp
	g++ -o motor.o -c motor.cpp
	
clean:
	$(RM) $(TARGET) 
