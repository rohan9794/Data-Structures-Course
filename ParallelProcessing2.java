/* need to incorporate the logic for following type of cases:
 * 2 5
 * 0 0 0 0 0
 * my output:
 * 0 0
 * 1 0
 * 0 0
 * 0 0
 * 0 0
 * 
 * correct output: 
 * 0 0
 * 1 0
 * 0 0 
 * 1 0
 * 0 0
 * */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;



public class ParallelProcessing2 {

	/**
	 * @param args
	 */
	private int numWorkers;
	int m;
    private long[] jobs;
    ArrayList<Thread> threads;
    Thread[] threads_array;
    int size;
    private int[] assignedWorker;
    private long[] startTime;
    private long[] finTemp;

    private FastScanner in;
    private PrintWriter out;
	public static void main(String[] args) {
		try {
			new ParallelProcessing2().solve();
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
//        threads= new ArrayList<Thread>();
        threads_array=new Thread[numWorkers];
        finTemp=new long[numWorkers];
        m = in.nextInt();
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
        for (int i = 0; i < m; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }
	
	private void assignJobs() {
		assignedWorker=new int[m];
		startTime=new long[m];
		if(numWorkers<=m){
			//below 3 lines added
			/*assignedWorker[0]=0;
			startTime[0]=0;
			threads.add(0, new Thread(0,jobs[0]));
//			threads[0]=new Thread(0,jobs[0]);
			finTemp[0]=jobs[0];
			int j=1;
			while(jobs[j-1]==0 && j<=m){
				assignedWorker[j]=0;
				startTime[j]=0;
				finTemp[0]=finTemp[0]+jobs[j];
				threads.get(0).finishTime=jobs[j];
				j++;
			}
			//make sure to fill the array list only upto the number of workers
		for(int i=1;i<numWorkers;i++){
			// add the logic of incorprating trailing zeroes as the job times in this loop
			threads.add(i, new Thread(i, jobs[j]));
//			threads[i]=new Thread(i, jobs[j]);
			assignedWorker[j]=i;
			finTemp[i]=jobs[j];
			startTime[j]=0;
			j++;
		}*/
			for(int i=0;i<numWorkers;i++){
				assignedWorker[i]=i;
				startTime[i]=0;
				threads_array[i]=new Thread(i,jobs[i]);
				finTemp[i]=jobs[i];
			}
		//let us convert the thread arraylist into an array of type thread only
		
//		Thread[] threads_array= new Thread[numWorkers];
//		threads_array= (Thread[]) threads.toArray();
//		System.out.println("threads length:"+threads_array.length);
		for(int i=numWorkers/2;i>=0;i--){
			siftDown(i, threads_array.length);
		}
        for(int i=numWorkers;i<m;i++){
        	Thread temp=extractMin();
        	assignedWorker[i]=temp.id;
        	startTime[i]=finTemp[temp.id];
        	finTemp[temp.id]=finTemp[temp.id]+jobs[i];
        	insert(new Thread(assignedWorker[i],finTemp[temp.id]));
        }}else{
//        	assignedWorker[0]=0;
//			startTime[0]=0;
        	for(int i=0;i<m;i++){
//        		if(assignedWorker[i]==0){
//        			assignedWorker[i]=assignedWorker[i-1];
//        		}else{
        		assignedWorker[i]=i;
//        		}
        		startTime[i]=0;
        	}
        }
    }

	private void siftDown(int n, int s){
        size=s;
        int maxIndex = n;
        int l = leftChild(n);
        if (l < size) {
            if (threads_array[l].finishTime < threads_array[maxIndex].finishTime) {
                maxIndex = l;
            }else if(threads_array[l].finishTime==threads_array[maxIndex].finishTime && threads_array[l].id<threads_array[maxIndex].id){
            	maxIndex = l;
            }
        }
        int r = rightChild(n);
        if (r < size) {
            if (threads_array[r].finishTime < threads_array[maxIndex].finishTime) {
                maxIndex = r;
            }else if(threads_array[r].finishTime == threads_array[maxIndex].finishTime && threads_array[r].id<threads_array[maxIndex].id){
            	maxIndex = r;
            }
        }
        if (n != maxIndex) {
            Thread temp = threads_array[n];
            threads_array[n] = threads_array[maxIndex];
            threads_array[maxIndex] = temp;
            siftDown(maxIndex, size);
        }
    }
	
private void siftUp(int n){
		
		while(n>0){
			if(threads_array[parent(n)].finishTime>threads_array[n].finishTime){
				Thread temp=threads_array[n];
				threads_array[n]=threads_array[parent(n)];
				threads_array[parent(n)]=temp;
				n=parent(n);
			}/*else if(threads_array[parent(n)].finishTime==threads_array[n].finishTime && threads_array[parent(n)].id>threads_array[n].id){
				Thread temp=threads_array[n];
				threads_array[n]=threads_array[parent(n)];
				threads_array[parent(n)]=temp;
				n=parent(n);
			}*/
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
		Thread result=threads_array[0];
		threads_array[0]=threads_array[size-1];
		size=size-1;
		siftDown(0, size);
		return result;
		
	}
	
	private void insert(Thread t){
		
		if(size==numWorkers){
			return;
		}
		size+=1;
		threads_array[size-1]=t;
		siftUp(size-1);
	}
	public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
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
