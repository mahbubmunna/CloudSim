package cloudsim;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;

public class KMeansClustering {

	private List<Cloudlet> clist;
	private List<dcCharacteristics> dlist;

	public void kmeans(List<Cloudlet>clist, List<dcCharacteristics> dlist) {
	    this.setClist(clist);
	    this.setDlist(dlist);
	    
	    //initializer
	    int i, n = 0;
	    boolean flag;
	    
	    //create latSum and longSum array for calculating new Centroid 
	    //create Centroids, previous Centroid holder, dataPoint latitude, longitude
	    double latSum1=0, latSum2=0, longSum1=0, longSum2=0;	    
	    double dis1, dis2, cenLat1, cenLat2, cenLon1, cenLon2;	   
	    double lat, lon, a1, a2, b1, b2;
	    
	    //take initial datacenters as Centroid
	    dcCharacteristics ax = (dcCharacteristics) dlist.get(0);
	    dcCharacteristics bx = (dcCharacteristics) dlist.get(1);
	    
	    //populate with latitude and longitude to the Centroids
	    cenLat1 = ax.getLatitude();
	    cenLon1 = ax.getLongitude();
	    cenLat2 = bx.getLatitude();
	    cenLon2 = bx.getLongitude();
	    
	    //create the clusters for Datacenter and Cloudlets
	    ArrayList<Integer> cluster1 = new ArrayList<Integer>();
	    ArrayList<Integer> cluster2 = new ArrayList<Integer>();
	    ArrayList<Integer> cloudletCluster1 = new ArrayList<Integer>();
	    ArrayList<Integer> cloudletCluster2 = new ArrayList<Integer>();
	    
	    //may not needed
	    int cl1[] = new int[clist.size()], cl2[] = new int[clist.size()];
	    
	    do {
	    	//reseter
	        latSum1=0;
	        latSum2=0;
	        longSum1=0;
	        longSum2=0;
	        
	        cluster1.clear();
	        cluster2.clear();
	        cloudletCluster1.clear();
	        cloudletCluster2.clear();
	        
	        //increment the iterator counter
	        n++;
	        
	        //maybe not needed
	        int k = 0, j = 0;
	        
	        //take datacenter from datacenter list and make cluster
	        for (i = 0; i < clist.size(); i++) {
	            dcCharacteristics d = (dcCharacteristics) dlist.get(i);

	            lat = d.getLatitude();
	            lon = d.getLongitude();
	            
	            dis1 = KMeansClustering.getDistance(cenLat1, cenLon1, lat, lon);
	            
	            dis2 = KMeansClustering.getDistance(cenLat2, cenLon2, lat, lon);
	            
	            
	            if (dis1 <= dis2) {
	                cluster1.add(d.getId()); 
	                cl1[k] = i;
	                k++;
	            } else {
	                cluster2.add(d.getId());
	                
	                cl2[j] = i;
	                j++;
	            }
	            
	            //take cloudlet from cloudlet list and cluster them
	            clet c = (clet) clist.get(i);
	            lat = c.getLatitude();
	            lon = c.getLongitude();
	            dis1 = KMeansClustering.getDistance(cenLat1, cenLon1, lat, lon);
	            dis2 = KMeansClustering.getDistance(cenLat2, cenLon2, lat, lon);
	            
	            if(dis1<=dis2) {
	            	cloudletCluster1.add(c.getCloudletId());
	            } else {
	            	cloudletCluster2.add(c.getCloudletId());
	            }
	            
	          
	        }
	        // calculate all sum of datacenter datapoints and determine new centroid
	        System.out.println();
	        for (i = 0; i < k; i++) {
	            //clet clt = (clet) clist.get(cl1[i]);
	            dcCharacteristics dcC = (dcCharacteristics) dlist.get(cl1[i]);
	            latSum1 += dcC.getLatitude();
	            longSum1 += dcC.getLongitude();
	        }
	        for (i = 0; i < j; i++) {
	        	dcCharacteristics dcC1 = (dcCharacteristics) dlist.get(cl2[i]);
	            latSum2 += dcC1.getLatitude();
	            longSum2 += dcC1.getLongitude();
	        }
	        
	        a1 = cenLat1;
	        a2 = cenLon1;
	        b1 = cenLat2;
	        b2 = cenLon2;
	        cenLat1 = latSum1/k;
	        cenLon1 = longSum1/k;
	        cenLat2= latSum2/j;
	        cenLon2 = longSum2/j;
	        //make flag false if cluster become stable
	        flag =! (cenLat1 == a1 && cenLon1 == a2 && cenLat2 == b1 && cenLon2 == b2);
	        
	        //printing new cluster after every iteration
	        System.out.println("After iteration " + n + " , cluster 1 :\n");    //printing the clusters of each iteration
	        
	        System.out.print(cluster1);
	
	        System.out.println("\n");
	        System.out.println("After iteration " + n + " , cluster 2 :\n");

	        System.out.print(cluster2);
	        
	        if(n>100) break;
	    } while (flag);//Exit loop if cluster become stable
	    
	    
	    //printing final clusters
	    System.out.println();
	    System.out.println("Final cluster 1 :\n");            // final clusters
	    
	    System.out.print("Datacenter id: ");
	        System.out.println(cluster1);
	        System.out.print("Cloudlet id:");
	    System.out.println(cloudletCluster1);
	
	    System.out.println();
	    System.out.println("Final cluster 2 :");
	    
	    	System.out.print("Datacenter id: ");
	        System.out.println(cluster2);
	        System.out.print("Cloudlet id:");
	        System.out.println(cloudletCluster2);
	    
	}

	//Distance calculating method
  public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
      KMeansClustering kl = new KMeansClustering();
  	double R = 6371; // Radius of the earth in km
      double dLat = kl.deg2rad(lat2-lat1);  // deg2rad below
      double dLon = kl.deg2rad(lon2-lon1); 
      double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(kl.deg2rad(lat1)) * Math.cos(kl.deg2rad(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
      double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a)); 
      double d = R * c; // Distance in km
      return d;
  }

  //distance calculating method helper method
  double deg2rad(double deg) {
      return deg * (Math.PI/180);
  }
  
  
  //These maybe unnecessary 
	public List<Cloudlet> getClist() {
		return clist;
	}
	
	public void setClist(List<Cloudlet> clist) {
		this.clist = clist;
	}

	public List<dcCharacteristics> getDlist() {
		return dlist;
	}

	public void setDlist(List<dcCharacteristics> dlist) {
		this.dlist = dlist;
	}
}