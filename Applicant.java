import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class Applicant extends Thread{
    private int id;
    private Random random = new Random();
    
    private AtomicBoolean response = new AtomicBoolean(false); // variable for tracking recruiter's response
    private AtomicBoolean interviewresponse = new AtomicBoolean(false); // variable for tracking interviewer's response
    
    private boolean pass = false; // variable to determine whether applicant was passed by recruiter
    private boolean interview = false; // variable to determine whether applicant was passed by interviewer
    
    //Constructor
    Applicant(int id)
    {
        setName("Applicant-"+id);
        this.id = id;
        start();
    }
    
    //Setter methods
    public void setResponse()
    {
        response.set(true);
    }
    public void setInterviewResponse()
    {
        interviewresponse.set(true);
    }
    public void setPass()
    {
        pass = true;
    }
    public void setInterview()
    {
        interview = true;
    }
    
    //Getter methods
    public int getID()
    {
        return id;
    }
    
    //Message logging function
    public void msg(String m)
    {
        System.out.println("["+(System.currentTimeMillis()-JobInterview.time)+"] "+getName()+": "+m);
    }
    
    public void run()
    {
        try 
        {
            //It takes some time to prepare the application
            Thread.sleep(random.nextInt(2000));
        } 
        catch (InterruptedException ex) 
        {
            System.out.println("Interruption exception occurred");
        }
        msg("Submitted application");
        Recruiter.applicationQueue.add(this); // add applicant to the applicationQueue of Recruiter
        
        //Busy waiting until the recruiter gives the response
        while(response.get() == false);
        
        //If the applicant got passed by the recruiter
        if(pass == true)
        {
            int chances = random.nextInt(10);
            if(chances <= 3)    //eager applicants
            {
                this.setPriority(MAX_PRIORITY);
                try
                {
                    Thread.sleep(random.nextInt(2000));
                }
                catch(InterruptedException ex)
                {
                    System.out.println("Interrupted exception occurred");
                }
                this.setPriority(NORM_PRIORITY);
                msg("I am eager and I am ready");
            }
            else    //procrastinating applicants
            {
                try
                {
                    Thread.sleep(random.nextInt(2000));
                    yield();
                    Thread.sleep(random.nextInt(2000));
                    yield();
                    msg("I procrastinated and I am ready");
                }
                catch(InterruptedException ex)
                {
                    System.out.println("Interrupted exception occurred");
                }
            }
            Interviewer.readyList.add(this);
            try
            {
                //Waiting for interviewer to call for the interview
                Thread.sleep(999999);
            }
            //Interrupted when interviewer calls for the interview
            catch(InterruptedException ex)
            {
                //Busy wait until the interviewer's positive response
                while(interviewresponse.get() == false);
                
                //If the applicant got passed in the interview
                if(interview == true)
                {
                    try
                    {
                        msg("I have got the job! I am celebrating");
                        Thread.sleep(random.nextInt(2000));
                    }
                    catch(InterruptedException exc)
                    {
                        System.out.println("Interrupted Exception occurred");
                    }
                }
                
                //Wait for the previous applicant threads to finish their execution
                for(int i=0; i<JobInterview.numApplicants; i++)
                {
                    Applicant a = JobInterview.applicants[i];
                    if(this.id < a.getID() && a.isAlive())
                    {
                        try 
                        {
                            a.join();
                        } 
                        catch (InterruptedException ex1) 
                        {
                            System.out.println("Interrupted Exception occurred");
                        }
                    }
                }
            }
        }
    }
}
