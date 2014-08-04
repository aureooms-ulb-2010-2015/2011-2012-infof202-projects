#include "vehicle.hpp"
#include <vector>


int main(){

	// cannot use Motor in thermal_vehicule nor electric_vehicule, respectively using specific subclass, but we can instantiate it
	Motor m;
	
	//following are used in example
	Thermal_motor m0;
	Diesel_motor m00;
	Gasoline_motor m01;
	Electric_motor m1;
	Asynchronous_electric_motor m10;
	Synchronous_electric_motor m11;
	
	// cannot istantiate Vehicule abstract class
	//not instantiating all possibilities, just some of them
	Thermal_vehicle v00;
		v00.setMotor(m0);
	
	Thermal_vehicle v02;
		v02.setMotor(m00);
	
	Thermal_vehicle v03;
		v03.setMotor(m01);
	
	Electric_vehicle v10;
		v10.addMotor(m1);
		
	Electric_vehicle v11;
		v11.addMotor(m1);		
		v11.addMotor(m10);
		
	Electric_vehicle v12;
		v12.addMotor(m10);
		v12.addMotor(m11);
	
	Hybrid_vehicle h;
		h.setMotor(m0);
		h.addMotor(m1);
		
	Serial_hybrid_vehicle h0;
		h0.addMotor(m10);
		h0.addMotor(m10);
		
	Parallel_hybrid_vehicle h1;
		h1.setMotor(m00);
		h1.addMotor(m10);
		
	Mild_hybrid<Independant_hybrid_vehicle> h20;
		h20.addMotor(m1);
		h20.addMotor(m11);
		
	Full_hybrid<PowerSplit_hybrid_vehicle> h30;
		h30.addMotor(m1);
		h30.addMotor(m10);
	

	
    vector<Vehicle*> vehicles;
    
    vehicles.push_back(&v00);
    vehicles.push_back(&v02);
    vehicles.push_back(&v03);
    vehicles.push_back(&v10);
    vehicles.push_back(&v11);
    vehicles.push_back(&v12);
    vehicles.push_back(&h);
    vehicles.push_back(&h0);
    vehicles.push_back(&h1);
    vehicles.push_back(&h20);
    vehicles.push_back(&h30);
    
    vector<Vehicle*>::iterator vehicle_iterator;
    
    for(vehicle_iterator = vehicles.begin(); vehicle_iterator!=vehicles.end(); ++vehicle_iterator)
    {
        cout << **vehicle_iterator << endl;
    }
    
    v00.run(10);
    v02.run(20);
    v03.run(90);
    v10.run(30);
    v11.run(80);
    v12.run(40);
    h.run(70);
    h0.run(50);
    h1.run(60);
    h20.run(60);
    h30.run(70);
    return 0;
}
