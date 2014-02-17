import com.bardo.proyectjane.bigboss.BigBoss;


public class init {
	
	public static BigBoss bigBoss;
	
	
	public static void main(String[] args) {
		bigBoss = new BigBoss();
		bigBoss.initializeModules();
		
	}

}
