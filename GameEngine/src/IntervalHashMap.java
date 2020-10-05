import java.util.ArrayList;

public class IntervalHashMap {

	private ArrayList<ArrayList<Interval>> hashMap;
	private double minIntervalX1;    // minIntervalX1 is the smallest X1 interval value in a level; it represents the beginning of the first mapping in the hash map
	private double maxIntervalX2;    
	private double mapChunkSizeX;
	
	static public double landingPrecision;
	
	public IntervalHashMap(ArrayList<Interval> intervals, double minIntervalX1, double maxIntervalX2, double mapChunkSizeX) {
		
		this.minIntervalX1 = minIntervalX1;
		this.maxIntervalX2 = maxIntervalX2;
		this.mapChunkSizeX = mapChunkSizeX;
		System.out.println("maxIntervalX2 = "+this.maxIntervalX2);
		int maxHashCode = genHashCodeFromKeyX(maxIntervalX2);
		System.out.println("maxHashCode = "+maxHashCode);
		this.hashMap = new ArrayList<ArrayList<Interval>>();
		for (int r = 0; r < maxHashCode; r++) {
			this.hashMap.add(null);
		}
		System.out.println("hashMap has "+this.hashMap.size()+" map chunks");
		
		// Places Intervals in the hash map
		for (int i = 0; i < intervals.size(); i++) {
			Interval interval = intervals.get(i);
			double x1 = interval.getX1(null, 1);
			double x2 = interval.getX2(null, 1);
			int hashCodes[] = genHashCodesFromKeysX(x1 - landingPrecision, x2 + landingPrecision);
			if (hashCodes == null) {
				System.out.println("init hash map: hashCodes is null for an interval");
			} else {
				System.out.println("hashCodes has length: "+hashCodes.length);
			}
			for (int t = 0; t < hashCodes.length; t++) {
				// Initialize a hash table mapping's bucket list if it's not yet initialized
				if (hashMap.get(hashCodes[t]) == null) {
					hashMap.set(hashCodes[t], new ArrayList<Interval>());
				}
				hashMap.get(hashCodes[t]).add(interval);
			}
		}
		
	}
	
	// generate hash code for a single key x-position
	public int genHashCodeFromKeyX(double x) {
		int hashCode = (int) Math.floor( (x - minIntervalX1) / mapChunkSizeX );
		return hashCode;
	}
	
	// generate a list of hash codes for key x-positions from x1 to x2
	public int[] genHashCodesFromKeysX(double x1, double x2) {
		//System.out.println("genHashCodesFromKeysX: x1 = "+x1+" and x2 = "+x2);
		int hashCode1 = (int) Math.floor( (Math.min(x1,x2) - minIntervalX1) / mapChunkSizeX );
		int hashCode2 = (int) Math.floor( (Math.max(x1,x2) - minIntervalX1) / mapChunkSizeX );
		//System.out.println("hashCode1 = "+hashCode1);
		//System.out.println("hashCode2 = "+hashCode2);
		int hashCodes[] = new int[hashCode2 - hashCode1 + 1];
		for (int i = hashCode1; i <= hashCode2; i++) {
			hashCodes[i - hashCode1] = i;
		}
		
		
		return hashCodes;
	}
	
	public ArrayList<Interval> getIntervalList(int hashCode) {
		// if hashCode is in region less than smallest Interval X1, return null to indicate no Intervals
		if (hashCode < 0) {
			return null;
		}
		return hashMap.get(hashCode);
	}
	
	public double getMapChunkSizeX() {
		return this.mapChunkSizeX;
	}
	
}
