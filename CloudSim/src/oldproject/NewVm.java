package oldproject;

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Vm;

public class NewVm extends Vm {
	private double cost;
	private boolean status;
	public NewVm(
			int id,
			int userId,
			int mips,
			int numberOfPes,
			int ram,
			long bw,
			long size,
			String vmm,
			
			 //double status,
			CloudletScheduler cloudletScheduler,
			double cost,
			boolean status) {
		super(id, userId, mips, numberOfPes, ram, bw, size, vmm,  cloudletScheduler);
		this.cost = cost;
		this.status = status;
	}
	
	public  void setCost(double cmmCost) {
		this.cost = cmmCost;		
	}
	
	public  double getCost() {
		return cost;	
	}
		
		public  void setStatus(boolean status) {
			this.status = status;		
		}
		
		public  boolean getStatus() {
			return status;
		}	
}