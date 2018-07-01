package cloudsim;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.UtilizationModel;

public class clet extends Cloudlet{
	private double latitude;
	private double longitude;

	public clet(int cloudletId, long cloudletLength, int pesNumber,
			long cloudletFileSize, long cloudletOutputSize,
			UtilizationModel utilizationModelCpu,
			UtilizationModel utilizationModelRam,
			UtilizationModel utilizationModelBw, double latitude, double longitude) {
		super(cloudletId, cloudletLength, pesNumber, cloudletFileSize,
				cloudletOutputSize, utilizationModelCpu, utilizationModelRam,
				utilizationModelBw);
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
