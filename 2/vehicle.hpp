#include "motor.hpp"

#define max_electric_motors 2
class Vehicle{

	private:
		string name;

	protected:
		virtual void setName(string name);
		
    public:
        
        Vehicle();
        virtual void run(unsigned int miles) = 0;
        virtual string toString() = 0;
        virtual float getConsumption() = 0;
        virtual bool canRun() = 0;
        virtual string printMotors() = 0;
        virtual string getName();

};

class Thermal_vehicle: virtual public Vehicle{

    private:
    
        Thermal_motor* motor;
        bool has_motor;

    public:
        
		Thermal_vehicle();
        virtual void run(unsigned int miles);
        virtual string toString();
        virtual float getConsumption();
        virtual Thermal_motor& getMotor();
        virtual void setMotor(Thermal_motor &motor);
		virtual void removeMotor();
        virtual bool canRun();
        virtual string printMotors();
};


class Electric_vehicle: virtual public Vehicle{

    private:
    
        Electric_motor* motors [max_electric_motors];
        unsigned int number_of_electric_motors;
        
        
	public:
	
		Electric_vehicle();
		virtual void run(unsigned int miles);
        virtual string toString();
        virtual string printMotors();
        virtual float getConsumption();
        virtual Electric_motor& getMotor(unsigned int index);
        virtual void addMotor(Electric_motor &motor);
        virtual void removeMotor(Electric_motor &motor);
        virtual bool canRun();
};

class Hybrid_vehicle: public Thermal_vehicle, public Electric_vehicle{

	private:
		
		float thermal_electric_rate;
		
    protected:
    
    	virtual void setRate(float rate);
    	
    public:
        
        Hybrid_vehicle();
        virtual string toString();
        virtual string printMotors();
        virtual Thermal_motor& getMotor();
        virtual Electric_motor& getMotor(unsigned int index);
        virtual void removeMotor();
        virtual void removeMotor(Electric_motor &motor);
        virtual float getRate();
        virtual float getConsumption();
        virtual bool canRun();
        virtual void run(unsigned int miles);
        
};

class Independant_hybrid_vehicle: public Hybrid_vehicle{

	public:
		
		Independant_hybrid_vehicle();
};

class Serial_hybrid_vehicle: public Hybrid_vehicle{

	public:
		
		Serial_hybrid_vehicle();
};

class Parallel_hybrid_vehicle: public Hybrid_vehicle{

	public:
		
		Parallel_hybrid_vehicle();
};

class PowerSplit_hybrid_vehicle: public Hybrid_vehicle{

	public:
		
		PowerSplit_hybrid_vehicle();
};

template <class Hybrid_subclass>
class Mild_hybrid :public Hybrid_subclass{
	
	public:
		
		Mild_hybrid(){};
		virtual string getName(){
			return Hybrid_subclass::getName() + " (mild)";
		};
};

template <class Hybrid_subclass>
class Full_hybrid : public Hybrid_subclass{
	
	public:
		
		Full_hybrid(){};
        virtual bool canRun(){
			return  Electric_vehicle::canRun();
		};
		virtual string getName(){
			return Hybrid_subclass::getName() + " (full)";
		};
};

std::ostream &operator<<(std::ostream &out, Vehicle &vehicle);
