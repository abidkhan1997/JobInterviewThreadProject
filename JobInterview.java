import java.util.concurrent.atomic.AtomicInteger;

public class JobInterview {
    
    public static int numRecruiters = 2;
    public static int numApplicants = 20;
    public static int numInterviewers = 1;
    public static int numJobs = 10;
    
    public static long time = System.currentTimeMillis();
    
    //Busy wait variable is used as an atomic variable so that it can be used in busy wait loop
    public static AtomicInteger busywaitvar = new AtomicInteger();
    
    //Array of applicants, recruiters, and interviewers
    public static Applicant applicants[] = new Applicant[numApplicants];
    public static Recruiter recruiters[] = new Recruiter[numRecruiters];
    public static Interviewer interviewers[] = new Interviewer[numInterviewers];
   
    //Boolean return type function that checks if any of the applicant thread is still alive
    public static boolean checkAnyApplicantAlive()
    {
        for(int i=0; i<numApplicants; i++)
        {
            if(applicants[i].isAlive())
            {
                return true;
            }
        }
        return false;
    }
    
    public static void main(String[] args) throws InterruptedException
    {        
        if(args.length != 4 && args.length != 0)
        {
            System.out.println("Please enter all number of arguments");            
        }
        else
        {
            if(args.length == 4)
            {
                numApplicants = Integer.parseInt(args[0]);
                numRecruiters = Integer.parseInt(args[1]);
                numInterviewers = Integer.parseInt(args[2]);
                numJobs = Integer.parseInt(args[3]);
            }

            //Creating applicant threads
            for(int i=0; i<numApplicants; i++)
            {
                applicants[i] = new Applicant(i+1);
            }

            //Waiting for atleast application to be submitted
            busywaitvar.set(Recruiter.applicationQueue.size());
            while(busywaitvar.get() == 0)
            {
                busywaitvar.set(Recruiter.applicationQueue.size());
            }
            
            //Creating Recruiter threads
            for(int i=0; i<numRecruiters; i++)
            {
                recruiters[i] = new Recruiter(i+1);
            }
            
            //Waiting for threads to finish the execution
            for(int i=0; i<numRecruiters; i++)
            {
                recruiters[i].join();
            }
            
            //Waiting for atleast one applicant thread to be ready for the interview
            busywaitvar.set(Interviewer.readyList.size());
            
            //Creating interviewer threads
            for(int i=0; i<numInterviewers; i++)
            {
                interviewers[i] = new Interviewer(i+1);
            }
        }
    }
}
