package cloudsim;

import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;


public class KMeansCluster {
	private List<Cloudlet> clist;
	private List<dcCharacteristics> dlist;

	public ResultListsObj kmeans(List<Cloudlet>clist, List<dcCharacteristics> dlist) {
	    this.setClist(clist);
	    this.setDlist(dlist);
	    
	    //Determine cluster number K
	    final int numberOfClusters = determineK();
	    
	    //Initializer,  Datapoint latitude and longitude
	    int iteratorCounter = 0, rightCluster = 0, counter = 0;
	    boolean flag;
	    double latitude, longitude, leastDistance = 0;
	    
	    //Latitude and Longitude Sum Array for calculating new Centroid and distanceHolders
	    double[] latitudeSum = new double[numberOfClusters],
	    		 longitudeSum = new double[numberOfClusters],
	    		 distance = new double[numberOfClusters];
	    
	    //Centroids and previous Centroids holder
	    Centroid[] centroid = new Centroid[numberOfClusters],
	    		preCentroid = new Centroid[numberOfClusters];
	    
	    //Take initial datacenters as Centroids 
	    dcCharacteristics[] initialDc = new dcCharacteristics[numberOfClusters];
	    
	    //Clusters of Datacenters and Cloudlets
	    List<List<dcCharacteristics>> dcCluster = new ArrayList<List<dcCharacteristics>>();
	    List<List<clet>> clCluster = new ArrayList<List<clet>>();
	    for (int i = 0; i < numberOfClusters; ++i)
	    {
	    	dcCluster.add(new ArrayList<dcCharacteristics>());
	    	clCluster.add(new ArrayList<clet>());
	    }
	    
	    //Populate centroid
	    for (int i = 0; i < numberOfClusters; ++i) {
	    	initialDc[i] = (dcCharacteristics) dlist.get(i);
	    }
	    for (int i = 0; i < numberOfClusters; ++i) {
	    	centroid[i] = new Centroid(initialDc[i].getLatitude(), initialDc[i].getLongitude());
	    	preCentroid[i] = new Centroid(0, 0);
	    }
	    
	    //Make cluster based on current centroid
	    do {
	    	//reseter
	    	for (int i = 0; i < numberOfClusters; ++i) {
	    		latitudeSum[i] = 0;
	    		longitudeSum[i] = 0;
	    		dcCluster.get(i).clear();
		    	clCluster.get(i).clear();
	    		
	    		
	    	}
	    	
	    	//Take datacenter from datacenter list and make cluster
	    	for (int i = 0; i < dlist.size(); ++i) {
	    		
	    		
	    		//get the distance of datacenter datapoints from centroids and determine the right cluster
	    		dcCharacteristics dc = (dcCharacteristics) dlist.get(i);
	    		latitude = dc.getLatitude();
	    		longitude = dc.getLongitude();
	    		for (int j = 0; j < numberOfClusters; ++j) {
	    			distance[j] = KMeansCluster.getDistance(centroid[j].getLatitude(), centroid[j].getLongitude(), latitude, longitude);
	    			if (j > 0) {
	    				if (distance[j] < leastDistance) {
	    					leastDistance = distance[j];
	    					rightCluster = j;
	    				}
	    			} else {
	    				leastDistance = distance[j];
	    				rightCluster = j;
	    			}
	    		}
	    		dcCluster.get(rightCluster).add(dlist.get(i));
	    		
	    	//get the distance of cloudlet datapoints from centroids and determine the right cluster
	    		clet c = (clet) clist.get(i);
	    		latitude = c.getLatitude();
	    		longitude = c.getLongitude();
	    		for (int j = 0; j < numberOfClusters; ++j) {
	    			distance[j] = KMeansCluster.getDistance(centroid[j].getLatitude(), centroid[j].getLongitude(), latitude, longitude);
	    			if (j > 0) {
	    				if (distance[j] < leastDistance) {
	    					leastDistance = distance[j];
	    					rightCluster = j;
	    				}
	    			} else {
	    				leastDistance = distance[j];
	    				rightCluster = j;
	    			}
	    		}
	    		clCluster.get(rightCluster).add(c);
	    		
	    	}
	    	
	    	
	    	//calculate all sum of datacenter datapoints and determine new centroid
	    	counter = 0;
	    	for (List <dcCharacteristics> cluster : dcCluster ) {
	    		for (dcCharacteristics dc : cluster) {
	    			latitudeSum[counter] += dc.getLatitude();
	    			longitudeSum[counter] += dc.getLongitude();
	    		}
	    		
	    		//store previousCentroid 
	    		preCentroid[counter].setLatitude(centroid[counter].getLatitude());
	    		preCentroid[counter].setLongitude(centroid[counter].getLongitude());

	    		//determine new centroid
	    		centroid[counter].setLatitude(latitudeSum[counter] / cluster.size()); 
	    		centroid[counter].setLongitude(longitudeSum[counter] / cluster.size());
	    		++counter;

	    	}
	    	
	    	
	    	//make flag false if cluster become stable
	    	counter = 0;
	    	for (int i = 0; i < numberOfClusters; ++i) {
	    		if (centroid[i].getLatitude() == preCentroid[i].getLatitude()
	    				&& centroid[i].getLongitude() == preCentroid[i].getLongitude()) ++counter;
	    	}
	    	if (counter == numberOfClusters) flag = false;
	    	else flag = true;
	    	//System.out.println(counter);
	    	
	    	//Print new cluster after every iteration
	    	System.out.println("Iteration number " + ++iteratorCounter + ":");
	    	counter = 0;
	    	for (List <dcCharacteristics> cluster : dcCluster) {
	    		System.out.print("Cluster" + ++counter + ": ");
	    		for (dcCharacteristics dc : cluster) 
	    			System.out.print(dc.getId() + " ");
	    		System.out.println();
	    	}
	    	
	    } while (flag && iteratorCounter != 100);
	    
	    //Printing final clusters
	    System.out.println();
	    System.out.println("Final Clusters");
	    //datacenter clusters
	    counter = 0;
	    for (List <dcCharacteristics> cluster : dcCluster) {
	    		System.out.print("Datacenter Cluster " + ++counter + ": ");
	    		for (dcCharacteristics dc : cluster) {
	    			System.out.print(dc.getId() + " ");	
	    			System.out.println(dc.getLatitude());
	    			System.out.println(dc.getLongitude());
	    		}
	    				
	    		System.out.println();
    	}
	    
	    //cloudlet clusters
	    counter = 0;
	    for (List <clet> cluster : clCluster)
	    {
	    	System.out.print("Cloudlet Cluster" + ++counter + ": ");
	    	for (clet cl : cluster)
	    		System.out.print(cl.getCloudletId() + " ");
	    	System.out.println();
	    }
	    
	    //arranging the result list for binding perfectly 
	    List<dcCharacteristics> tempDc = new ArrayList<>();
	    List<clet> tempCl = new ArrayList<>();
	    dlist.clear();
	    clist.clear();
	    int min = 0, max = 0;
	    boolean dcTrue = true;
	    for (int i = 0; i < dcCluster.size(); ++i) {
	    	//taking the least cluster size of same position
	    	if (dcCluster.get(i).size() < clCluster.get(i).size()) {
	    		min = dcCluster.get(i).size();
	    		max = clCluster.get(i).size();
	    		dcTrue = false; // flag to work with TempCl list
	    	}
	    	else {
	    		min = clCluster.get(i).size();
	    		max = dcCluster.get(i).size();
	    		dcTrue = true; // flag to work with TempDc list
	    	}
	    	//maping based on least cluster size
	    	for (int j = 0; j < min; ++j) {
	    		dlist.add(dcCluster.get(i).get(j));
	    		clist.add(clCluster.get(i).get(j));
	    	}
	    	for (int j = min; j < max; ++j) {
	    		//stores the extra data points which are not getting clusters
	    		if (dcTrue) tempDc.add(dcCluster.get(i).get(j));
	    		else tempCl.add(clCluster.get(i).get(j));
	    	}
	    }
	    
	    //joint the temp to dlist and clist to make it full
	    dlist.addAll(tempDc);
	    clist.addAll(tempCl);
	    
	    //make a resultList object to return that for further binding
	    ResultListsObj resultList = new ResultListsObj();
	    resultList.setDc(dlist);
	    resultList.setCl(clist);
	    
	    return resultList;
	    
	    
	    
//	    //Test
//	    System.out.println("Centroid Values:");
//	    for (Centroid cen : centroid) {
//	    	System.out.println(cen.getLatitude() + " " + cen.getLongitude());
//	    }
//	    System.out.println("\npreCentroid Values:");
//	    for (Centroid cen : preCentroid) {
//	    	System.out.println(cen.getLatitude() + " " + cen.getLongitude());
//	    }
//	    System.out.println("Flag: " + flag);
	    
	
	}
	
	public int determineK() {
	    int datapoints = clist.size() + dlist.size();
		return (int) Math.round(Math.sqrt(datapoints/2));
	}
	
	//Distance calculating method
	  public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
	      KMeansCluster kl = new KMeansCluster();
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
