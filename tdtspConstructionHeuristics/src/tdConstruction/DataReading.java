package tdConstruction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Scanner;

public class DataReading {
	String type;
	String filePath;
	int nbLocations = -1;
	int nbTimeSteps = -1;
	double durationTimestep = -1;
	String fileName;
	Scanner file;
	
	public DataReading(String filePath) {
		this.filePath = filePath;
	}
	
	public Integer getnbLocations() {
		if (nbLocations != -1)
			return nbLocations;
		return null;
	}
	
	public Integer getnbTimeSteps() {
		if (nbTimeSteps != -1)
			return nbTimeSteps;
		return null;
	}
	
	public Integer getdurationTimeStep() {
		if (durationTimestep != -1)
			return (int) durationTimestep;
		return null;
	}
	
	public Double getdurationTimeStepCordeau() {
		if (durationTimestep != -1)
			return durationTimestep;
		return null;
	}
	
	public double[][][] getDistanceFctCordeau(){
		double[][][] distanceFct = null;
		fileName = filePath;
		double[][] distanceFct_Cordeau;
		double[][] speed = new double[3][3];
		this.nbTimeSteps = 3;
		durationTimestep = Double.MAX_VALUE;
		try {
			file = new Scanner(new File(fileName));
			file.useLocale(Locale.US);
			file.skip("N:");
			this.nbLocations = file.nextInt()+2;
			file.next();file.next();file.next();
			distanceFct_Cordeau = new double[nbLocations][nbLocations];
			distanceFct = new double[nbLocations][nbLocations][this.nbTimeSteps];
			
			for(int i=0;i<nbLocations;i++) {
				for(int j=0;j<nbLocations;j++) {
						distanceFct_Cordeau[i][j]=file.nextDouble();
				}
			}
			
			//System.out.println(distanceFct_Cordeau[0][6] +" " + distanceFct_Cordeau[6][15] +" " + distanceFct_Cordeau[15][14] +" "+ distanceFct_Cordeau[14][3] +" "+ distanceFct_Cordeau[3][1] +" "+ distanceFct_Cordeau[1][11] +" "+ distanceFct_Cordeau[11][8] +" "+ distanceFct_Cordeau[8][2] +" "+ distanceFct_Cordeau[2][12] +" "+ distanceFct_Cordeau[12][4] +" "+ distanceFct_Cordeau[4][7] +" "+ distanceFct_Cordeau[7][9] +" "+ distanceFct_Cordeau[9][13] +" "+ distanceFct_Cordeau[13][10] +" "+ distanceFct_Cordeau[10][5] +" "+ distanceFct_Cordeau[5][0] +" ");
			for (int i=0;i< 3; i++) {
				file.nextLine();
			}
			
			file.nextDouble();
			this.durationTimestep = file.nextDouble();
			
			for (int i=0;i< 5; i++) {
				file.nextLine();
			}
			
			for(int j = 0;j<3;j++) {
				for(int t = 0;t<3;t++) {
					speed[t][j] = file.nextDouble();


				}
			}
			for (int i=0;i< 9; i++) {
				file.nextLine();
			}
			
			int[] zone = new int[nbLocations];
			file.nextInt();
			zone[0] = file.nextInt();
			
			for(int i=0;i<nbLocations-1;i++) {
				zone[i+1] = file.nextInt();
				file.nextLine();
			}

			file.close();
			
			for(int i = 0;i<this.nbLocations;i++) {
				for(int j = 0;j<this.nbLocations;j++) {
					for(int t = 0;t<this.nbTimeSteps;t++) {
						// Changed that the speed is always the best congestion factor on the way
						if(distanceFct_Cordeau[i][j] / speed[t][zone[j]-1]< distanceFct_Cordeau[i][j] / speed[t][zone[i]-1])
							distanceFct[i][j][t] = distanceFct_Cordeau[i][j] / speed[t][zone[j]-1];
						else
							distanceFct[i][j][t] = distanceFct_Cordeau[i][j] / speed[t][zone[i]-1];
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return distanceFct;
		
	}
	
public double[][][] getDistanceFctMelgarejo(){
		try {
			file = new Scanner(new File(filePath));
			file.useLocale(Locale.US);
			this.nbLocations = file.nextInt();		
			this.nbTimeSteps = file.nextInt();
			this.durationTimestep = file.nextInt();
			double[][][] distanceFct = new double[this.nbLocations][this.nbLocations][this.nbTimeSteps];
			
			
			for(int i=0;i<nbLocations;i++) {
				for(int j=0;j<nbLocations;j++) {
					for(int s=0;s<this.nbTimeSteps;s++) {
						distanceFct[i][j][s]=file.nextInt();

					}
				}
			}
			file.close();
			return distanceFct;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public double[][][] getDistanceFctRifki(){
		try {
			file = new Scanner(new File(filePath));
			file.useLocale(Locale.US);
			this.nbLocations = file.nextInt();		
			this.nbTimeSteps = file.nextInt();
			this.durationTimestep = file.nextInt();
			double[][][] distanceFct = new double[this.nbLocations][this.nbLocations][this.nbTimeSteps];
			
			for(int i=0;i<=nbLocations;i++) {
				file.nextLine();
			}
			for(int i=0;i<nbLocations;i++) {
				for(int j=0;j<nbLocations;j++) {
					for(int s=0;s<this.nbTimeSteps;s++) {
						distanceFct[i][j][s]=file.nextInt();
						if(i!= 0 && j!= 0) {
							distanceFct[i][j][s] += 180;
						}
					}
				}
			}
			file.close();
			return distanceFct;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	public double[][][] getDistanceFctTimeIndependent(){
		try {
			file = new Scanner(new File(filePath));
			file.useLocale(Locale.US);
			file.nextLine();
			String a = (String) file.next();			
			while(a.equals("COMMENT") || a.equals("COMMENT:")) {
				file.nextLine();
				a = file.next();
			}
			file.nextLine();
			file.next();
			// Changed DIMENSION : to DIMENSION: when it differs
			this.nbLocations = file.nextInt();
			double[][][] distanceFct = new double[this.nbLocations][this.nbLocations][1];
			file.nextLine();file.nextLine();file.nextLine();
			double[] x = new double[nbLocations];
			double[] y = new double[nbLocations];
			
			for(int i=0;i<nbLocations;i++) {
				file.next();
				x[i] = file.nextDouble();
				y[i] = file.nextDouble();
			}
			
			
			for(int i=0;i<nbLocations;i++) {
				for(int j=0;j<nbLocations;j++) {
					distanceFct[i][j][0]= Math.sqrt((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
				}
			}
			file.close();
			return distanceFct;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
}
