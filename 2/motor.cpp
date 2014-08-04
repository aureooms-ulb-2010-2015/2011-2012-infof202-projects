#include "motor.hpp"


//MOTOR
Motor::Motor():name("motor"),consumption(1){}
string Motor::toString(){
	stringstream result;
	result << this->getName() << " (average consumption : " << this->getConsumption() << ")";
	return result.str();
}
string Motor::getName(){return this->name;}
int Motor::getConsumption(){return this->consumption;}
void Motor::setName(string name){this->name = name +" "+this->name;}
void Motor::setConsumption(int consumption){this->consumption = consumption;}

	//THERMAL MOTOR
	Thermal_motor::Thermal_motor(){

		this->setName("thermal");

	}
		//GASOLINE MOTOR
		Gasoline_motor::Gasoline_motor(){

			this->setName("gasoline");

		}

		//DIESEL MOTOR
		Diesel_motor::Diesel_motor(){

			this->setName("diesel");

		}
	//ELECTRIC MOTOR	
	Electric_motor::Electric_motor(){

		this->setName("electric");

	}
		//ASYNC e MOTOR
		Asynchronous_electric_motor::Asynchronous_electric_motor(){

			this->setName("asynchronous");


		}
		//SYNC e MOTOR
		Synchronous_electric_motor::Synchronous_electric_motor(){

			this->setName("synchronous");


		}





std::ostream &operator<<(std::ostream &out, Motor &motor){
    out << motor.toString();
    return out;
}
