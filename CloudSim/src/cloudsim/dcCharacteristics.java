package cloudsim;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;

public class dcCharacteristics extends DatacenterCharacteristics{
	private double latitude;
	private double longitude;
	
	public dcCharacteristics(String architecture, String os, String vmm,
			List<? extends Host> hostList, double timeZone, double costPerSec,
			double costPerMem, double costPerStorage, double costPerBw, double latitude, double longitude) {
		super(architecture, os, vmm, hostList, timeZone, costPerSec, costPerMem,
				costPerStorage, costPerBw);
		// TODO Auto-generated constructor stub
		this.latitude = latitude;
		this.longitude = longitude;
	}
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	public double getLatitude(){
		return latitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	public double getLongitude(){
		return longitude;
	}
}
