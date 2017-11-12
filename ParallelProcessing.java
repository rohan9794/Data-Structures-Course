package DS;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/* failing test case: 2 5
 * 0 2 0 4 5
 * actual output: 0 0; 0 0;1 0; 1 0; 0 2*/

public class ParallelProcessing {
	private int numWorkers;
	int m;
    private long[] jobs;
    private Thread[] threads;
    int size;
    private int[] assignedWorker;
    private long[] startTime;
    private long[] finTemp;

    private FastScanner in;
    private PrintWriter out;
	public static void main(String[] args) {
		try {
			new ParallelProcessing().solve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readData() throws IOException {
        numWorkers = in.nextInt();
        size=numWorkers;
        //constraint defined below
        if(numWorkers<1 || numWorkers>Math.pow(10,5)){
            System.exit(0);
        }
        threads= new Thread[numWorkers];
        finTemp=new long[numWorkers];
        m = in.nextInt();
//        size=numWorkers;
        //constraint defined below
        if(m<1 || m>Math.pow(10,5)){
            System.exit(0);
        }
        jobs = new long[m];
        for (int i = 0; i < m; ++i) {
            jobs[i] = in.nextLong();
        }
        //constraint defined below
        for(int i=0;i<m;i++){
            if(jobs[i]<0 || jobs[i]>Math.pow(10,9)){
                System.exit(0);
            }
        }
    }
	
	private void writeResponse() {
//		System.out.println("writing final response");
        for (int i = 0; i < m; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }
	
	private void assignJobs() {
        // TODO: replace this code with a faster algorithm.
		assignedWorker=new int[m];
		startTime=new long[m];
		if(numWorkers<=m){
			//below 3 lines added
			assignedWorker[0]=0;
			startTime[0]=0;
			threads[0]=new Thread(0,jobs[0]);
			finTemp[0]=jobs[0];
			int j=1;
			while(jobs[j-1]==0 && j<=m){
				assignedWorker[j]=0;
				startTime[j]=0;
				finTemp[0]=finTemp[0]+jobs[j];
				threads[0].finishTime=jobs[j];
				j++;
			}
		for(int i=1;i<numWorkers;i++){
			threads[i]=new Thread(i, jobs[j]);
			//below if-else added
			/*if(jobs[i-1]==0){
				assignedWorker[i]=assignedWorker[i-1];
				threads[i-1].finishTime=jobs[i];
				finTemp[i]=finTemp[i-1];
			}else{*/
			assignedWorker[j]=i;
			//threads[i]=new Thread(i, jobs[i]);
			finTemp[i]=jobs[j];
//			}
			startTime[j]=0;
			j++;
//			int nullCount=Collections.frequency(Array.asList(threads), null);
			//finTemp[i]=jobs[i];
		}
		//testing for loop below
		/*for(int i=0;i<numWorkers;i++){
			System.out.println("2 lines of test..");
			System.out.print(threads[i].id+" "+threads[i].finishTime);
			System.out.println();
		}*/
		System.out.println("threads length:"+threads.length);
		for(int i=numWorkers/2;i>=0;i--){
			siftDown(i, threads.length);
		}
        for(int i=j;i<m;i++){
        	Thread temp=extractMin();
        	assignedWorker[i]=temp.id;
        	startTime[i]=finTemp[temp.id];
        	finTemp[temp.id]=finTemp[temp.id]+jobs[i];
        	//testing statement below
//        	System.out.println("this is test "+assignedWorker[i]+" "+startTime[i]);
        	insert(new Thread(assignedWorker[i],finTemp[temp.id]));
        }}else{
        	assignedWorker[0]=0;
			startTime[0]=0;
        	for(int i=1;i<m;i++){
        		if(assignedWorker[i-1]==0){
        			assignedWorker[i]=assignedWorker[i-1];
        		}else{
        		assignedWorker[i]=i;
        		}
        		startTime[i]=0;
        	}
        }
       /* assignedWorker = new int[jobs.length];
        startTime = new long[jobs.length];
        long[] nextFreeTime = new long[numWorkers];
        for (int i = 0; i < jobs.length; i++) {
            int duration = jobs[i];
            int bestWorker = 0;
            for (int j = 0; j < numWorkers; ++j) {
                if (nextFreeTime[j] < nextFreeTime[bestWorker])
                    bestWorker = j;
            }
            assignedWorker[i] = bestWorker;
            startTime[i] = nextFreeTime[bestWorker];
            nextFreeTime[bestWorker] += duration;
        }*/
    }
	
	private void siftDown(int n, int s){
        size=s;
        int maxIndex = n;
        int l = leftChild(n);
        if (l < size) {
            if (threads[l].finishTime < threads[maxIndex].finishTime) {
                maxIndex = l;
            }else if(threads[l].finishTime==threads[maxIndex].finishTime && threads[l].id<threads[maxIndex].id){
            	maxIndex = l;
            }
        }
        int r = rightChild(n);
        if (r < size) {
            if (threads[r].finishTime < threads[maxIndex].finishTime) {
                maxIndex = r;
            }else if(threads[r].finishTime == threads[maxIndex].finishTime && threads[r].id<threads[maxIndex].id){
            	maxIndex = r;
            }
        }
        if (n != maxIndex) {
            Thread temp = threads[n];
            threads[n] = threads[maxIndex];
            threads[maxIndex] = temp;
            siftDown(maxIndex, size);
        }
    }

	private void siftUp(int n){
		
		while(n>0){
			if(threads[parent(n)].finishTime>threads[n].finishTime){
				Thread temp=threads[n];
				threads[n]=threads[parent(n)];
				threads[parent(n)]=temp;
				n=parent(n);
			}else if(threads[parent(n)].finishTime==threads[n].finishTime && threads[parent(n)].id>threads[n].id){
				Thread temp=threads[n];
				threads[n]=threads[parent(n)];
				threads[parent(n)]=temp;
				n=parent(n);
			}
			n--;
		}
	}
	public int parent(int i){
		return (i-1)/2;
	}
	public int leftChild(int i) {
		return 2 * i + 1;
	}

	public int rightChild(int i) {
		return 2 * i + 2;
	}

	private Thread extractMin(){
//		size=threads.length; //check if size can be defined in some global area
		Thread result=threads[0];
		threads[0]=threads[size-1];
		size=size-1;
		siftDown(0, size);
		return result;
		
	}
	
	private void insert(Thread t){
		
		if(size==numWorkers){
			return;
		}
		size+=1;
		threads[size-1]=t;
		//work somewhere here
		siftUp(size-1);
	}
	public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
//        size=threads.length;
        readData();
        assignJobs();
        writeResponse();
        out.close();
    }
	
	static class Thread{
		int id;
		long finishTime;
		 public Thread(int i,long f){
			 this.id=i;
			 this.finishTime=f;
		 }
	}
	
	static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
        
        public long nextLong() throws IOException {
        	return Long.parseLong(next());
        }
    }
}
