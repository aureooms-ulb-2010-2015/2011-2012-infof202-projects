#include <iostream>
#include <string>
#include <sstream>
#include <cstdlib>

using namespace std;

class Motor{

    private:
    
        string name;
        int consumption;
        
    protected:
    
    	virtual void setName(string name);
    	virtual void setConsumption(int consumption);

    public:
        
        Motor();
        virtual string toString();
        virtual string getName();
        virtual int getConsumption();

};

class Thermal_motor: public Motor{

    public:
        
		Thermal_motor();

};

class Gasoline_motor: public Thermal_motor{

    public:
        
        Gasoline_motor();

};

class Diesel_motor: public Thermal_motor{

    public:
        
        Diesel_motor();

};



class Electric_motor: public Motor{

    public:
        
		Electric_motor();

};


class Asynchronous_electric_motor: public Electric_motor{

    public:
        
        Asynchronous_electric_motor();

};


class Synchronous_electric_motor: public Electric_motor{

    public:
        
        Synchronous_electric_motor();

};



std::ostream &operator<<(std::ostream &out, Motor &motor);
