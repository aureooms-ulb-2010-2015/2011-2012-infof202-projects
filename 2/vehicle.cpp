#include "vehicle.hpp"
     

//Vehicle abstract super class

Vehicle::Vehicle(){}
void Vehicle::setName(string name){
	this->name = name;
}
string Vehicle::getName(){
	return this->name;
}


	//Thermal_vehicle
	
	Thermal_vehicle::Thermal_vehicle():motor(NULL),has_motor(false){
		this->setName("Thermal vehicle");
	}
	   		
	void Thermal_vehicle::run(unsigned int miles){
		if (this->canRun()){
			cout << this->getName() << " running :" << "\n";
			cout << miles*this->getConsumption() << " liter(s) consumed" << "\n";
		}
		else{
			cout << this->getName() << " cannot run :" << "\n";
			cout << "thermal motor missing" << "\n";
		}
	}
		 
	string Thermal_vehicle::toString(){
		stringstream result;
		result << this->getName() << "\n" << this->printMotors();
		return result.str();
	}
	
	string Thermal_vehicle::printMotors(){
		string result = "no thermal motor";
		if (this->has_motor){
			result = this->getMotor().toString();
		}
		return result;
	}
	
	float Thermal_vehicle::getConsumption(){
		if (!this->has_motor){
			return -1;
		}
		else{
			return this->getMotor().getConsumption();
		}
	}
	
	Thermal_motor& Thermal_vehicle::getMotor(){
		return *this->motor;
	}
	void Thermal_vehicle::setMotor(Thermal_motor &motor){
		this->motor = &motor;
		this->has_motor = true;
	}
	
	void Thermal_vehicle::removeMotor(){
		this->motor = NULL;
		this->has_motor = false;
	}
	
	bool Thermal_vehicle::canRun(){
		if (!this->has_motor){
			return false;
		}
		else{
			return true;
		}
	}
	
	
	//Electric_vehicle
		
	Electric_vehicle::Electric_vehicle():number_of_electric_motors(0){
		this->setName("Electric vehicle");
	}
	
	void Electric_vehicle::run(unsigned int miles){				
		if (this->canRun()){
			cout << this->getName() << " running :" << "\n";
			cout << miles*this->getConsumption() << " watt(s) consumed" << "\n";
		}
		else{
			cout << this->getName() << " cannot run :" << "\n";
			cout << "electric motor missing (one required)" << "\n";
		}
	}
		 
	string Electric_vehicle::toString(){
		stringstream result;
		result << this->getName() << "\n" << this->printMotors();
		return result.str();
	}
	
	string Electric_vehicle::printMotors(){
		string result = "";
		if (this->number_of_electric_motors == 0){
			result+="no electric motor";
		}
		else{
			result += this->getMotor(0).toString();
			unsigned int index = 1;
			while (index < this->number_of_electric_motors){
				result += "\n" + this->getMotor(index).toString();
				index++;
			}
		}
		return result;
	}
	
	Electric_motor& Electric_vehicle::getMotor(unsigned int index){
		
		return *this->motors[index];
	}
	
	void Electric_vehicle::addMotor(Electric_motor &motor){
		this->motors[this->number_of_electric_motors++] = &motor;
	}
	
	void Electric_vehicle::removeMotor(Electric_motor &motor){
		unsigned int index = 0;
		while (index < this->number_of_electric_motors){
			if (this->motors[index] == &motor){
				this->motors[index] = this->motors[--this->number_of_electric_motors];
				break;
			}
			index++;
		}
	}
	
	float Electric_vehicle::getConsumption(){
		
		float consumption = 0;
		unsigned int index = 0;
		while (index < this->number_of_electric_motors){
			consumption += (this->getMotor(index).getConsumption());
			index++;
		}
		if ( index == 0){
			return -1;
		}
		else{
			consumption /= index;
			return consumption;
		}
		
	}
		
	bool Electric_vehicle::canRun(){
		if(!this->number_of_electric_motors > 0){
			return false;
		}
		else{
			return true;
		}
	}
        

	//Hybrid_vehicle
	
	Hybrid_vehicle::Hybrid_vehicle():thermal_electric_rate(-1){
		this->setName("Hybrid vehicle");
	}


	string Hybrid_vehicle::toString(){
		stringstream result;
		result << this->getName() << "\n" << this->printMotors();
		return result.str();
	}
	string Hybrid_vehicle::printMotors(){
		return Thermal_vehicle::printMotors() +"\n"+ Electric_vehicle::printMotors();
	}
	
	void Hybrid_vehicle::setRate(float rate){
		this->thermal_electric_rate = rate;
	}
	
	float Hybrid_vehicle::getRate(){
		return this->thermal_electric_rate;
	}
	
	Thermal_motor& Hybrid_vehicle::getMotor(){
		return Thermal_vehicle::getMotor();
	}
	Electric_motor& Hybrid_vehicle::getMotor(unsigned int index){
		return Electric_vehicle::getMotor(index);
	}
	void Hybrid_vehicle::removeMotor(){
		Thermal_vehicle::removeMotor();
	}
	void Hybrid_vehicle::removeMotor(Electric_motor &motor){
		Electric_vehicle::removeMotor(motor);
	}
	
	bool Hybrid_vehicle::canRun(){
		if (this->getRate() == -1){
			cout << this->getName() << " cannot run :" << "\n";
			cout << "consumption rate is not defined" << "\n";
			return false;
		}
		else{
			return  Electric_vehicle::canRun() && Thermal_vehicle::canRun();
		}
	}	

	void Hybrid_vehicle::run(unsigned int miles){
		if (this->canRun()){
			cout << this->getName() << " running :" << "\n";
			if (Thermal_vehicle::canRun()){
				cout << miles*this->getRate()*Thermal_vehicle::getConsumption() << " liter(s) consumed" << "\n";
				cout << miles*(1-this->getRate())*Electric_vehicle::getConsumption() << " watt(s) consumed" << "\n";
			}
			else{
				cout << miles*Electric_vehicle::getConsumption() << " watt(s) consumed" << "\n";
			}
		}
		
		else{
			if (!Electric_vehicle::canRun()){
				cout << this->getName() << " cannot run :" << "\n";
				cout << "electric motor missing (one required)" << "\n";
			}
			else if(!Thermal_vehicle::canRun()){
				cout << this->getName() << " cannot run :" << "\n";
				cout << "thermal motor missing" << "\n";
			}
		}
	}
	
	float Hybrid_vehicle::getConsumption(){
		cerr << "using getConsumption on an hybrid vehicle is useless as it uses super class method in method run" << endl;
		return -1;
	}
	
	//Independant
	Independant_hybrid_vehicle::Independant_hybrid_vehicle(){
		this->setName("Independant hybrid vehicle");
		this->setRate(0.8);
	}
	//Serial
	Serial_hybrid_vehicle::Serial_hybrid_vehicle(){
		this->setName("Serial hybrid vehicle");
		this->setRate(0.6);
	}
	//Parallel
	Parallel_hybrid_vehicle::Parallel_hybrid_vehicle(){
		this->setName("Parallel hybrid vehicle");
		this->setRate(0.65);
	}
	//PowerSplit
	PowerSplit_hybrid_vehicle::PowerSplit_hybrid_vehicle(){
		this->setName("PowerSplit hybrid vehicle");
		this->setRate(0.7);
	}
	

std::ostream &operator<<(std::ostream &out, Vehicle &vehicle) {
    out << vehicle.toString();
    return out;
}
