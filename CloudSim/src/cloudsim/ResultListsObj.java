package cloudsim;

import java.util.ArrayList;
import java.util.List;
import org.cloudbus.cloudsim.Cloudlet;

public class ResultListsObj {
	private List<dcCharacteristics> dc;
	private List<Cloudlet> cl;
	
	public ResultListsObj() {
		dc = new ArrayList<>();
		cl = new ArrayList<>();
	}

	public List<dcCharacteristics> getDc() {
		return dc;
	}

	public void setDc(List<dcCharacteristics> dc) {
		this.dc = dc;
	}

	public List<Cloudlet> getCl() {
		return cl;
	}

	public void setCl(List<Cloudlet> cl) {
		this.cl = cl;
	}

}
