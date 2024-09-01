import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Recruiter extends Thread{
    private int id;
    private Random random = new Random();
    private static int reviewedapplications = 0; // variable to track the number of applications reviewed
    
    //It stores the applicant number. All the application
    public static ArrayList<Applicant> applicationQueue = new ArrayList<Applicant>();
    
    //Mutex lock variable declared as an atomic variable is used to ensure mutually exclusive access to critical section
    private static AtomicBoolean mutex = new AtomicBoolean(false);
    
    //Constructor
    Recruiter(int id)
    {
        setName("Recruiter-"+id);
        this.id = id;
        start();
    }
    
    public int getID()
    {
        return id;
    }
    
    //Message Logging function
    public void msg(String m)
    {
        System.out.println("["+(System.currentTimeMillis()-JobInterview.time)+"] "+getName()+": "+m);
    }
    
    public void run()
    {
        int acceptchances;
        
        //Keep on running until all applications have not been reviewed
        while(reviewedapplications != JobInterview.numApplicants)
        {
            //Mutex lock before entering critical section
            while(mutex.get() == true);
            mutex.set(true);
            if(applicationQueue.size() != 0)
            {
                try
                {
                    //Get the first applicant from the application queue
                    Applicant currentApplicant = applicationQueue.remove(0);
                    acceptchances = random.nextInt(10);
                    
                    try
                    {
                        Thread.sleep(random.nextInt(2000));
                    } 
                    catch (InterruptedException ex) 
                    {
                        System.out.println("Interruption exception occurred");
                    }
                    
                    //20% probability of not passing the resume phase and set the response of respective applicant thread
                    if(acceptchances <= 2)
                    {
                        msg("Rejected application " + currentApplicant.getID());
                    }
                    else
                    {
                        msg("Accepted application " + currentApplicant.getID());
                        currentApplicant.setPass();
                    }
                    currentApplicant.setResponse();
                    reviewedapplications++;
                }
                catch(Exception ex)
                {
                    mutex.set(false);
                    continue;
                }
            }
            //Mutex unlock before exiting from critical section
            mutex.set(false);
        }
        msg("I am leaving");
    }
}
