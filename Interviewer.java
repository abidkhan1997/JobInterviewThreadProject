import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Interviewer extends Thread{
    private int id;
    private Random random = new Random();
    private static int accepted;
    
    //Mutex lock variable declared as an atomic variable is used to ensure mutually exclusive access to critical section
    private static AtomicBoolean mutex = new AtomicBoolean(false);
    
    //It stores the applicants that are ready for the interview
    public static ArrayList<Applicant> readyList = new ArrayList<Applicant>();
    
    //Constructor
    Interviewer(int id)
    {
        setName("Interviewer-"+id);
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
        //Keep on running until all the applicants have not terminated
        while(JobInterview.checkAnyApplicantAlive())
        {   
            //Mutex lock before entering critical section
            while(mutex.get() == true);
            mutex.set(true);
            if(readyList.size() != 0)
            {
                try
                {
                    //System.out.print("from " + readyList);
                    Applicant candidate = readyList.remove(0);
                    //System.out.println(" took out " + candidate);
                    msg("Interviewing applicant " + candidate.getID());
                    
                    //Interrupt the applicant for interview
                    candidate.interrupt();
                    Thread.sleep(random.nextInt(2000));
                    
                    int acceptchances = random.nextInt(10);
                    //Probability of getting job is 70% and check if all number of jobs have already been filled
                    if(acceptchances <= 7 && (accepted < JobInterview.numJobs))
                    {
                        msg("Accepted applicant " + candidate.getID());
                        candidate.setInterview();
                        accepted++;
                    }
                    else
                    {
                        msg("Rejected applicant " + candidate.getID());
                    }
                    //Set the interview response for the applicant
                    candidate.setInterviewResponse();
                }
                catch(InterruptedException ex)
                {
                    System.out.println("Interrupted Exception occurred");
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
