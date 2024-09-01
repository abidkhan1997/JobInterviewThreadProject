Classes and Their Roles
Applicant (Thread):

Represents a job applicant.
Waits for their turn to be interviewed by a Recruiter.
After the interview, the applicant might perform some actions (e.g., leave the interview room or wait for the results).
Recruiter (Thread):

Represents a recruiter who conducts interviews.
Interviews applicants one by one.
Might decide the result of the interview and notify the applicant.
Interview (Shared Resource):

Represents the actual interview session.
Acts as a synchronized resource where only one Applicant can be interviewed by the Recruiter at a time.
JobInterview (Coordinator):

Coordinates the interaction between Applicant and Recruiter.
Manages the queue of applicants waiting for an interview.
Ensures proper scheduling and resource management.

How the Program Works
Initialization:

A JobInterview object is created to act as the shared resource that controls the interview process.
A Recruiter thread is started, which will continuously conduct interviews.
Multiple Applicant threads are created, each representing an applicant waiting for an interview.
Interview Process:

Each Applicant thread waits for its turn to be interviewed by calling jobInterview.attendInterview(this).
The JobInterview class manages the queue of applicants and ensures that only one applicant is interviewed at a time.
The Recruiter thread continuously checks if there are applicants to interview and if it can conduct the interview.
Synchronization and Communication:

The synchronized keyword ensures that access to the interview process is thread-safe, preventing race conditions.
The wait() and notifyAll() methods are used for inter-thread communication, ensuring that threads properly wait for their turn and notify others when they are done.
Key Points
Thread Safety: The use of synchronized blocks and methods ensures that shared resources are accessed safely by multiple threads.
Inter-thread Communication: wait() and notifyAll() are crucial for managing thread communication, especially in scenarios where threads need to wait for each other to complete tasks.
Concurrency Management: Proper handling of thread states and transitions (like waiting and notifying) is essential for creating an efficient multithreaded application.
This basic implementation can be expanded with more complex behaviors, such as adding multiple recruiters, more sophisticated interview scheduling, or handling different outcomes of interviews.
