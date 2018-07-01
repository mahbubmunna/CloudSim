package cloudsim;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
//import java.util.Map;
import java.util.Random;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerSpaceShared;
import cloudsim.clet;
//import org.cloudbus.cloudsim.datacenterBrokerNew;
import cloudsim.dcCharacteristics;
//import org.cloudbus.cloudsim.newBroker;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

/**
 *
 * @author Mahbub
 */
public class Main {
    
    private static List<Cloudlet> cloudletList;
    private static List<Vm> vmlist;
    public static List<dcCharacteristics> datacenterList = new ArrayList<dcCharacteristics>();
    
    public static void main(String[] args) throws Exception {
        int num_user = 100;
        Calendar calendar = Calendar.getInstance();
        boolean trace_flag = false;
        CloudSim.init(num_user, calendar, trace_flag);

  
        //Creating 10 Datacenters - each one has 1 host with 5 PEs
        int i;
        Datacenter[] dc = new Datacenter[10];
        for(i=0; i<10; i++){
        	//@SuppressWarnings("unused")
        	dc[i] = createDatacenter("DC"+i, 1);
        }
        
        DatacenterBroker brk = null;
        for(i=0; i<10; i++){
        	brk = createBroker("broker"+i);
        }
        vmlist = createVM(brk.getId(), 10, 1);
        cloudletList = createCloudlet(brk.getId(), 10, 1);
        
        brk.submitCloudletList(cloudletList);
        brk.submitVmList(vmlist);
        
        
        
        List<Integer> result1 = new ArrayList<Integer>();
        result1 = display();
        
        i=1;
        for(Integer r : result1){
        	brk.bindCloudletToVm(i, r-1);
        	i++;
        }
        
        CloudSim.startSimulation();        
        CloudSim.stopSimulation();
        
        display();
        printCloudletList(cloudletList);
        
        KMeansClustering kmc = new KMeansClustering();
        kmc.kmeans(cloudletList, datacenterList);
        
    }
    
    private static List<Vm> createVM(int userId, int vms, int idShift) {
        //Creates a container to store VMs. This list is passed to the broker later
        List<Vm> vlist = new LinkedList<Vm>();

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 1000;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        Vm[] vm = new Vm[vms];
        
        for (int i = 0; i < vms; i++) {
            vm[i] = new Vm(idShift + i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            vlist.add(vm[i]);
        }
        
        return vlist;
    }
    
    private static List<Cloudlet> createCloudlet(int userId, int cloudlets, int idShift) {
        // Creates a container to store Cloudlets
        List<Cloudlet> clist = new LinkedList<Cloudlet>();

        //cloudlet parameters
        long length = 100000;
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();
        
        clet[] cloudlet = new clet[cloudlets];
        double latitude; 
        double longitude;
        Random random = new Random();

        for (int i = 0; i < cloudlets; i++) {
        	 latitude = random.nextDouble()*180 -90;
             longitude = random.nextDouble()*360 -180;
            cloudlet[i] = new clet(idShift + i, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel, latitude, longitude);
            // setting the owner of these Cloudlets
            cloudlet[i].setUserId(userId);
            clist.add(cloudlet[i]);
        }
        
        return clist;
    }
    
    private static Datacenter createDatacenter(String name, int hostNumber) {
        
        
        List<Host> hostList = new ArrayList<Host>();
        List<Pe> peList1 = new ArrayList<Pe>();
        
        int mips = 1000;
        int hostId = 0;
        int ram = 16384;
        long storage = 1000000;
        int bw = 10000;
        
        peList1.add(new Pe(0, new PeProvisionerSimple(mips)));
        
        for (int i = 0; i < hostNumber; i++) {
            hostList.add(
                    new Host(
                    hostId,
                    new RamProvisionerSimple(ram),
                    new BwProvisionerSimple(bw),
                    storage,
                    peList1,
                    new VmSchedulerSpaceShared(peList1)));
            
            hostId++;
        }
        
        
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;		// the cost of using memory in this resource
        double costPerStorage = 0.1;	// the cost of using storage in this resource
        double costPerBw = 0.1;			// the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

        
        Random random = new Random();
        double latitude;
        double longitude;
        
        latitude = random.nextDouble()*180 -90;
        longitude = random.nextDouble()*360 -180;
        dcCharacteristics characteristics = new dcCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw, latitude, longitude);
        datacenterList.add(characteristics);
        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return datacenter;
    }
    
    private static DatacenterBroker createBroker(String name) {
        
        DatacenterBroker broker = null;
        try {
            broker = new DatacenterBroker(name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return broker;
    }

    /**
     * Prints the Cloudlet objects
     *
     * @param list list of Cloudlets
     * @throws Exception 
     */
    private static void printCloudletList(List<Cloudlet> list) throws Exception {
        int size = list.size();
        Cloudlet cloudlet;
        
        String indent = "    ";
        Log.printLine();
        Log.printLine("========== OUTPUT ==========");
        Log.printLine("Cloudlet ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time");
        
        DecimalFormat dft = new DecimalFormat("###.##");
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);
            
            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");
                
                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + indent + cloudlet.getVmId()
                        + indent + indent + indent + dft.format(cloudlet.getActualCPUTime())
                        + indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent + indent + dft.format(cloudlet.getFinishTime()));
            }
        }
//        datacenterBrokerNew dc = new datacenterBrokerNew("abcd");
//        Map<Integer, Integer> map = dc.getVmsToDatacentersMap();
//        Log.printLine(map);
    }
    
    
    public static List<Integer> display(){
  	  List<Integer> result = new ArrayList<Integer>();
    	clet c;
  	  dcCharacteristics n;
  	  double lat1, lat2, lon1, lon2, distance, shortestDistance;
  	  int nearestDatacenter=0, j;
  	  
  	  int[] usedDatacenter = new int[10];
        int countUsedDatacenter=0;
  	  
  	  for(int i=0; i<10; i++){
	    	  c = (clet) cloudletList.get(i);
	          lat1 = c.getLatitude();
	          lon1 = c.getLongitude();
	          shortestDistance = 1000000000;      
	          
	          for(j=0; j<10; j++){
		          int k;
	        	  for(k = 0; k<countUsedDatacenter; k++){
	        		  if(j==usedDatacenter[k]) {
	        			  break;
	        		  }
	        	  }
	        	  
	        	  if(k!=countUsedDatacenter) continue;
	        	  
		          n = datacenterList.get(j);
		          lat2 = n.getLatitude();
		          lon2 = n.getLongitude();
		          
		          distance = getDistance(lat1, lon1, lat2, lon2);

		         if(shortestDistance>distance){
		        	  shortestDistance = distance;
		        	  nearestDatacenter = j;
		          }
	          }
	          usedDatacenter[countUsedDatacenter] = nearestDatacenter;
	          countUsedDatacenter++;
	          n = datacenterList.get(nearestDatacenter);
	          int id = n.getId();
	          result.add(id);
	          Log.printLine("Cloudlet: " +(i+1)+",  nearest datacenter: "+ (nearestDatacenter+2) + ", Distance: "+ shortestDistance+ " km.");
  	  }
  	
  	return result;
  }

  

  public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
      Main cl = new Main();
  	double R = 6371; // Radius of the earth in km
      double dLat = cl.deg2rad(lat2-lat1);  // deg2rad below
      double dLon = cl.deg2rad(lon2-lon1); 
      double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(cl.deg2rad(lat1)) * Math.cos(cl.deg2rad(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
      double d = R * c; // Distance in km
      return d;
  }
  
  double deg2rad(double deg) {
      return deg * (Math.PI/180);
  }
      
}